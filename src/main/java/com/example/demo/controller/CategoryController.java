package com.example.demo.controller;


import com.example.demo.entity.Category;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>>  getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Optional<Category> categoriaBuscada = categoryService.getCategoryById(id);
        if (categoriaBuscada.isPresent()){
            return ResponseEntity.ok(categoriaBuscada.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.addCategory(category));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@RequestBody Category category) throws BadRequestException {
        Category categoryUpdated=categoryService.updateCategory(category);
        return ResponseEntity.ok(categoryUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletecategory(@PathVariable Long id ) throws ResourceNotFoundException {
        categoryService.deletecategory(id);
        return ResponseEntity.ok("Category with id "+ id+" was successfully eliminated. ");
    }
}
