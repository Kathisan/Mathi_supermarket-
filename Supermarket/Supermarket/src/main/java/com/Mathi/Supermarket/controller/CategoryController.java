package com.Mathi.Supermarket.controller;

import com.Mathi.Supermarket.model.Category;
import com.Mathi.Supermarket.repository.CategoryRepository;
import com.Mathi.Supermarket.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping("/add")
    public Category addCategory(@RequestParam("name") String name,
                                @RequestParam("image") MultipartFile imageFile) {

        String fileName = fileStorageService.storeFile(imageFile);

        Category category = new Category();
        category.setName(name);
        category.setImageUrl("/uploads/" + fileName);

        return categoryRepository.save(category);
    }
}

