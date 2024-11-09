package com.project.alims.service;

import com.project.alims.model.User;
import com.project.alims.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    public void sendEmail(String to, String name, String subject, String code, int emailCode) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);

        String containerMessage = "<p> Invalid Email Code sent to method <p>";
        if (emailCode == 1) {
            // Send OTP
            containerMessage = "<h1>Hello ALIMS, " + name + "!</h1>" +
                    "<p>To proceed with your account creation, please enter your One Time Password (OTP) on the verification page.</p>" +
                    "<p>Your OTP is <span class='code'>" + code + "</span>.</p>";

        } else if (emailCode == 2) {
            // Send Temp Password
            containerMessage = "<h1>Hello " + name + "!</h1>" +
                    "<p>To access your account, please use this temporary password. <span class='code'>" + code + "</span>.</p>" +
                    "<p>Please log in and replace this temporary password before it expires in 24 hours</p>" +
                    "<p>In the event that you did not request this, please file a report our website and change your password promptly</p>";
        }

        // Improved HTML content with medical-themed styles
        String htmlContent = "<html>" +
                "<head>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; background-color: #f0f8ff; padding: 20px; }" +
                ".container { background-color: #ffffff; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2); }" +
                "h1 { color: #004080; text-align: center; }" +
                "p { color: #333333; font-size: 16px; line-height: 1.5; }" +
                ".code { font-weight: bold; font-size: 20px; color: #007bff; padding: 10px; border: 2px solid #007bff; border-radius: 5px; display: inline-block; margin-top: 10px; }" +
                ".footer { margin-top: 20px; font-size: 14px; color: #777777; text-align: center; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                containerMessage +
                "</div>" +
                "<div class='footer'>" +
                "<p>Thank you for choosing ALIMS!</p>" +
                "</div>" +
                "</body>" +
                "</html>";

        // Set the HTML content to the message
        message.setContent(htmlContent, "text/html; charset=utf-8");

        javaMailSender.send(message);
    }


    public void sendVerification(String email, String name, String code) throws MessagingException {
        String subject = "Account Verification";
        sendEmail(email, name, subject, code, 1);
    }

    public ResponseEntity<String> addUser(User user) {
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

    public String forgotPassword(String email) throws MessagingException {
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
