package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.Product;
import com.Mathi.Supermarket.repository.ProductRepository;
import com.Mathi.Supermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestParam("name") String name,
                              @RequestParam("price") double price,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("expiryDate") LocalDate expiryDate){
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setExpiryDate(expiryDate);
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        return productService.updateProduct(id, productDetails);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam("query") String query) {

        return productService.searchProducts(query);
    }



}