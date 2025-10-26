package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.Product;
import com.Mathi.Supermarket.repository.ProductRepository;
import com.Mathi.Supermarket.service.FileStorageService;
import com.Mathi.Supermarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @PostMapping("/add")
    public Product addProduct(
            @RequestParam("name") String name,
            
            @RequestParam("quantity") double quantity,
            @RequestParam("unit") String unit,
            @RequestParam("brandId") Long brandId,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("expiryDate") String expiryDate,            @RequestParam("image") MultipartFile imageFile,
            @RequestParam(value = "allowFraction", defaultValue = "false") boolean allowFraction){

        LocalDate expDate = LocalDate.parse(expiryDate);
        String fileName = fileStorageService.storeFile(imageFile);

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setUnit(unit);
        product.setExpiryDate(expDate);
        product.setImageUrl("/uploads/" + fileName);
        product.setAllowFraction(allowFraction);

        Product saved = productService.addProduct(product, brandId, categoryId);

        System.out.println("Saved Product ID: " + saved.getId());
        return saved;
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

    @GetMapping("/alerts/lowstock")
    public List<Product> getLowStockProducts() {
        return productService.getLowStockProducts();
    }

    @GetMapping("/alerts/expiringsoon")
    public List<Product> getExpiringSoonProducts() {
        return productService.getExpiringSoonProducts();
    }

    @GetMapping("/brand/{brand}")
    public List<Product> getProductsByBrand(@PathVariable("brand") String brandName) {
        return productRepository.findByBrand_NameIgnoreCase(brandName);
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable("category") String categoryName) {
        return productRepository.findByCategory_NameIgnoreCase(categoryName);

    }

}