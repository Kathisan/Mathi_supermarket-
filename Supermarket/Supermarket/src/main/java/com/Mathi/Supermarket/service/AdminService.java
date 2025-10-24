package com.Mathi.Supermarket.service;

import com.Mathi.Supermarket.model.Admin;
import com.Mathi.Supermarket.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;


    // --- Check admin login credentials ---
    public boolean checkLogin(String username, String password) {
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            return admin.getPassword().equals(password); // simple password check
        }
        return false;
    }

    public boolean isUsernameTaken(String username) {
        return adminRepository.findByUsername(username).isPresent();
    }


    // --- Update password (for forgot password) ---
    public boolean updatePassword(String username, String newPassword) {
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            admin.setPassword(newPassword);
            adminRepository.save(admin);
            return true;
        }
        return false;
    }

}