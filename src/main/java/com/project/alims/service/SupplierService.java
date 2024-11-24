package com.project.alims.service;

import com.project.alims.model.Supplier;
import com.project.alims.model.User;
import com.project.alims.repository.SupplierRepository;
import com.project.alims.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository, UserRepository userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public List<Supplier> getFilteredSuppliers(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        User user = userOptional.get();
        String filteredSuppliers = user.getFilteredSuppliers();
        List<Supplier> allSuppliers = supplierRepository.findAll();
        if (filteredSuppliers == null || filteredSuppliers.isEmpty()) {
            return allSuppliers;
        }
        Set<Long> filteredSupplierIds = Arrays.stream(filteredSuppliers.split(","))
                .map(String::trim)
                .filter(id -> !id.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        return allSuppliers.stream()
                .filter(supplier -> !filteredSupplierIds.contains(supplier.getSupplierId()))
                .collect(Collectors.toList());
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Optional<Supplier> supplierOptional = supplierRepository.findById(id);
        if (supplierOptional.isPresent()) {
            Supplier supplier = supplierOptional.get();

            if(supplierDetails.getCompanyName() != null) supplier.setCompanyName(supplierDetails.getCompanyName());
            if(supplierDetails.getContactPerson() != null) supplier.setContactPerson(supplierDetails.getContactPerson());
            if(supplierDetails.getEmail() != null) supplier.setEmail(supplierDetails.getEmail());
            if(supplierDetails.getAddress() != null) supplier.setAddress(supplierDetails.getAddress());
            if(supplierDetails.getPhoneNumber() != null) supplier.setPhoneNumber(supplierDetails.getPhoneNumber());
            return supplierRepository.save(supplier);
        } else {
            throw new RuntimeException("Supplier not found");
        }
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
