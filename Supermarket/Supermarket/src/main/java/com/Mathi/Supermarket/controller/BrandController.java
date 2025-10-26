package com.Mathi.Supermarket.controller;


import com.Mathi.Supermarket.model.Brand;
import com.Mathi.Supermarket.repository.BrandRepository;
import com.Mathi.Supermarket.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/brands")
public class BrandController {

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @PostMapping("/add")
    public Brand addBrand(@RequestParam("name") String name,
                          @RequestParam("image") MultipartFile imageFile) {

        String fileName = fileStorageService.storeFile(imageFile);

        Brand brand = new Brand();
        brand.setName(name);
        brand.setImageUrl("/uploads/" + fileName);

        return brandRepository.save(brand);
        @Autowired
                private {/id}=null;
        delete
    }
}

