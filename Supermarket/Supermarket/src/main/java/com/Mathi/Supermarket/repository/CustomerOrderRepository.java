package com.Mathi.Supermarket.repository;

import com.Mathi.Supermarket.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findAllByOrderByIdDesc();


    long countByStatus(String status);


    List<CustomerOrder> findByStatus(String status);
}
