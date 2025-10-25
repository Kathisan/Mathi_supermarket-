package com.Mathi.Supermarket.repository;


import com.Mathi.Supermarket.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByBrand_NameIgnoreCase(String brandName);

    List<Product> findByCategory_NameIgnoreCase(String categoryName);

}