package com.Mathi.Supermarket.repository;

import com.Mathi.Supermarket.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // Find admin by username (for login)
    Optional<Admin> findByUsername(String username);

    // Check if username already exists
    boolean existsByUsername(String username);
}

