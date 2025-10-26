package com.Mathi.Supermarket.controller;


import com.Mathi.Supermarket.model.Brand;
import com.Mathi.Supermarket.model.Category;
import com.Mathi.Supermarket.repository.BrandRepository;
import com.Mathi.Supermarket.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AdminPageController {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // Handle form submission
    @PostMapping("/admin/add-brand-category")
    public String addBrandCategory(
            @RequestParam(required = false) String brandName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) MultipartFile brandImage,
            @RequestParam(required = false) MultipartFile categoryImage
    ) throws IOException {
        Brand brand = null; // define outside
        if (brandName != null && !brandName.isEmpty()) {
            brand = new Brand();
            brand.setName(brandName);

            // image handling
            if (brandImage != null && !brandImage.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + brandImage.getOriginalFilename();
                Path path = Paths.get("uploads/brands/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, brandImage.getBytes());
                brand.setImageUrl("/uploads/brands/" + fileName);
            }

            brandRepository.save(brand);
        }


        // ✅ Save Category only if category name is not empty
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            Category category = new Category();
            category.setName(categoryName.trim());

            // Save category image only if uploaded
            if (categoryImage != null && !categoryImage.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_" + categoryImage.getOriginalFilename();
                Path path = Paths.get("uploads/categories/" + fileName);
                Files.createDirectories(path.getParent());
                Files.write(path, categoryImage.getBytes());
                category.setImageUrl("/uploads/categories/" + fileName);
            }

            categoryRepository.save(category);
        }
        // Redirect back to the static HTML page
        return "redirect:/admin-add-brand-category.html";
    }

}
