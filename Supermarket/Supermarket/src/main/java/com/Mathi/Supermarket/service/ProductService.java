
package com.Mathi.Supermarket.service;

import com.Mathi.Supermarket.model.Product;
import com.Mathi.Supermarket.repository.ProductRepository;
import com.Mathi.Supermarket.model.Brand;
import com.Mathi.Supermarket.model.Category;
import com.Mathi.Supermarket.repository.BrandRepository;
import com.Mathi.Supermarket.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        existingProduct.setName(productDetails.getName());
        existingProduct.setPrice(productDetails.getPrice());
        existingProduct.setQuantity(productDetails.getQuantity());
        existingProduct.setUnit(productDetails.getUnit());
        existingProduct.setAllowFraction(productDetails.isAllowFraction());
        existingProduct.setExpiryDate(productDetails.getExpiryDate());
        return productRepository.save(existingProduct);
    }

    // --- SEARCH FUNCTION ---
    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    // --- LOW STOCK & EXPIRY ALERTS ---
    public List<Product> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(product -> product.getQuantity() < 10)
                .collect(Collectors.toList());
    }

   

    public Product addProduct(Product product, Long brandId, Long categoryId) {

        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + brandId));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + categoryId));

        product.setBrand(brand);
        product.setCategory(category);

        return productRepository.save(product);
    }


}

