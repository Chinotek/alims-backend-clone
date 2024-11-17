package com.project.alims.service;

import com.project.alims.model.User;
import com.project.alims.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final List<User> loggedInUsers;

    private static final int TEMP_PASSWORD_LENGTH = 10;
    private static final SecureRandom random = new SecureRandom();
    private static final String CHARACTER_SPACE = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender javaMailSender, List<User> loggedInUsers){
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.loggedInUsers = loggedInUsers;
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);  // Convert the integer to a String
    }

    // 1 - Send OTP
    // 2 - Send Temp Password
    public void sendEmail(String to, String name, String subject, String code, int emailCode) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);

        String containerMessage = "<p>Invalid Email Code sent to method.</p>";
        if (emailCode == 1) {
            // Send OTP
            containerMessage = "<h1> THANKS FOR SIGNING UP!</h1>" +
                    "<h2>Verify Your Email Address</h2>" +
                    "<hr style='border: 1px solid #179898; margin: 20px 0;'>" +
                    "<p>Hello " + name + ",</p>" +
                    "<p>Thank you for choosing ALIMS. Use the following OTP to verify your email address.</p>" +
                    "<p style='text-align: center; font-size: 40px; font-weight: 600; letter-spacing: 25px; color: #179898;'>" + code + "</p>" +
                    "<p>OTP is valid for <strong>5 minutes</strong>. Do not share this code with others</p>";
        } else if (emailCode == 2) {
            // Send Temp Password
            containerMessage = "<h1>Hello " + name + "!</h1>" +
                    "<p>To access your account, please use this temporary password:</p>" +
                    "<p style='font-size: 20px; font-weight: bold; color: #179898;'>" + code + "</p>" +
                    "<p>Please log in and replace this temporary password before it expires in 24 hours.</p>" +
                    "<p>If you did not request this, please file a report on our website and change your password promptly.</p>";
        }

        // Full email HTML template with embedded image
        String htmlContent = "<!DOCTYPE html>" +
                "<html lang='en'>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<style>" +
                "body { font-family: Arial, Helvetica, sans-serif; background: #ffffff; font-size: 14px; }" +
                ".container { max-width: 680px; margin: 0 auto; padding: 45px 30px 60px; background: #f4f7ff; border-radius: 30px; }" +
                "h1 { font-size: 25px; font-weight: 800; color: #004D40; text-align: center; }" +
                "h2 { font-size: 20px; font-weight: 500; color: #116F6F; text-align: center; }" +
                "p { font-size: 16px; font-weight: 500; color: #434343; line-height: 1.5; }" +
                ".footer { text-align: center; margin-top: 20px; color: #8c8c8c; font-size: 14px; }" +
                "img { width: 50px; height: auto; vertical-align: middle; display: inline-block; pointer-events: none; -webkit-user-drag: none; user-select: none; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<div style='display: flex; align-items: center;'>" + // Flex container for image and title
                "<img src='cid:logo' alt='Logo'>" +
                "<h1 style='font-size: 20px; display: inline-block; margin: 0; padding-left: 10px; padding-top: 10px'>Automated Laboratory Inventory Management System</h1>" +
                "</div>" + "<br>" +
                containerMessage +
                "</div>" +
                "<div class='footer'>" +
                "<p>Need help? Ask at <a href='mailto:alims@gmail.com' style='color: #499fb6; text-decoration: none;'>alims@gmail.com</a></p>" +
                "<p>&copy; 2024 Automated Laboratory Inventory Management System. All rights reserved.</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Create a MimeBodyPart for the HTML content
        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlContent, "text/html; charset=utf-8");

        // Create a MimeBodyPart for the inline image
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.attachFile("src/main/resources/static/logo.png"); // Path to your image
        imagePart.setContentID("<logo>");
        imagePart.setDisposition(MimeBodyPart.INLINE);

        // Combine the parts into a multipart message
        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(htmlPart);
        multipart.addBodyPart(imagePart);

        // Set the content of the message
        message.setContent(multipart);

        javaMailSender.send(message);
    }

    public void sendVerification(String email, String name, String code) throws MessagingException, IOException {
        String subject = "Account Verification";
        sendEmail(email, name, subject, code, 1);
    }

    public ResponseEntity<String> addUser(User user) throws IOException {
        User existingUsername = userRepository.findByUsername(user.getUsername());
        User existingEmail = userRepository.findByEmail(user.getEmail());

        if (existingUsername != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Username is already in use.");
        } else if (existingEmail != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email is already in use.");
        } else {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encryptedPassword = bcrypt.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            user.setStatus("Unverified Email");
            String otp = generateVerificationCode();
            user.setOtp(otp);
            try {
                sendVerification(user.getEmail(), user.getFirstName(), otp);
            } catch (MessagingException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("An error occurred while sending verification email.");
            }

            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully.");
        }
    }

    public ResponseEntity<String> addUserByAdmin(User user) {
        try {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encryptedPassword = bcrypt.encode(user.getPassword());
            user.setPassword(encryptedPassword);
            user.setStatus("Active");
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while registering the user.");
        }
    }

    public ResponseEntity<String> verifyUser(String email, String otp) {
        User unvUser = userRepository.findByEmailAndOtp(email, otp);
        if (unvUser != null) {
            unvUser.setStatus("Unapproved Account");
            userRepository.save(unvUser);
            return ResponseEntity.ok("User verified successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found or OTP is incorrect.");
    }

    public User findByEmailOrUsername(String email, String username) {
        return userRepository.findByEmailOrUsername(email, username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
    public User updateUser(Long userId, User userDetails) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getFirstName() != null) {
                user.setFirstName(userDetails.getFirstName());
            }
            if (userDetails.getMiddleName() != null) {
                user.setMiddleName(userDetails.getMiddleName());
            }
            if (userDetails.getLastName() != null) {
                user.setLastName(userDetails.getLastName());
            }
            if (userDetails.getDesignation() != null) {
                user.setDesignation(userDetails.getDesignation());
            }
            if (userDetails.getStatus() != null) {
                user.setStatus(userDetails.getStatus());
            }
            if (userDetails.getLabId() != null) {
                user.setLabId(userDetails.getLabId());
            }
            if (userDetails.getFilteredSuppliers() != null) {
                user.setFilteredSuppliers(userDetails.getFilteredSuppliers());
            }

            return userRepository.save(user);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    public ResponseEntity<String> changePassword(Long userId, String oldPassword, String newPassword) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        User user = userRepository.findByUserId(userId);

        if (user != null && bcrypt.matches(oldPassword, user.getPassword())) {
            String encryptedPassword = bcrypt.encode(newPassword);
            user.setPassword(encryptedPassword);
            userRepository.save(user);
            return new ResponseEntity<>("Successfully changed password", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
        }
    }
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }
    public String login(String identifier, String password) {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        User user = findByEmailOrUsername(identifier, identifier);

        if (user != null && bcrypt.matches(password, user.getPassword())) {
            if ("active".equalsIgnoreCase(user.getStatus())) {
                loggedInUsers.add(user);
                return "userId:" + user.getUserId() + ", role:" + user.getDesignation();
            } else if ("deleted".equalsIgnoreCase(user.getStatus())) {
                return "Login denied: Account has been deleted.";
            } else if ("unverified email".equalsIgnoreCase(user.getStatus())) {
                return "Login denied: Email not yet verified";
            } else if ("unapproved account".equalsIgnoreCase(user.getStatus())) {
                return "Login denied: Account not yet approved by Admin";
            }
        }
        return "Login denied: Invalid credentials.";
    }
    public ResponseEntity<String> logout(Long userId) {
        boolean removed = loggedInUsers.removeIf(user -> user.getUserId().equals(userId));
        if (removed) {
            return new ResponseEntity<>("Logout successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found or already logged out", HttpStatus.NOT_FOUND);
        }
    }

    public String forgotPassword(String email) throws MessagingException, IOException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String subject = "Temporary Password";

            String username = user.getUsername();
            String randomPassword = generateRandomPassword();

            sendEmail(email, username, subject, randomPassword, 2);

            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            String encryptedPassword = bcrypt.encode(randomPassword);
            user.setPassword(encryptedPassword);
            userRepository.save(user);

        } else {
            throw new RuntimeException("User not found with email: " + email);
        }
        return "Temporary password sent";
    }

    public static String generateRandomPassword() {
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);

        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTER_SPACE.length());
            password.append(CHARACTER_SPACE.charAt(index));
        }
        return password.toString();
    }

}
