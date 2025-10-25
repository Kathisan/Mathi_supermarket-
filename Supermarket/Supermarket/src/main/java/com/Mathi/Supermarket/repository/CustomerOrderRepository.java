package com.Mathi.Supermarket.repository;

import com.Mathi.Supermarket.model.CustomerOrder;
import com.Mathi.Supermarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAllByOrderByIdDesc();


    long countByStatus(String status);


    List<CustomerOrder> findByStatus(String status);

    // ================== ADD THIS: Fetch orders for a specific user ==================
    List<CustomerOrder> findByUserOrderByIdDesc(User user);
    // ==============================================================================

    // Optional: Fetch orders by user and status (e.g., pending orders for user)
    List<CustomerOrder> findByUserAndStatusOrderByIdDesc(User user, String status);
}

