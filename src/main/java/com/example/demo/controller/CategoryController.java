package com.example.demo.controller;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.entity.Category;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins="http://localhost:8090")
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    private AmazonS3 s3;

    @GetMapping
    public ResponseEntity<List<Category>>  getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> buscarUnaCategoria(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarCategoria(@RequestBody Category category) {
        String base64Image = (category.getImagen() != null) ? category.getImagen() : "";
        ObjectMetadata objectMetadata = new ObjectMetadata();

        try {
            byte[] binaryData = Base64.getDecoder().decode(base64Image);


            String key = category.getName() + "_" + UUID.randomUUID().toString() + ".jpg";

            objectMetadata.setContentLength(binaryData.length);
            s3.putObject("1023c13-grupo3", key, new ByteArrayInputStream(binaryData), objectMetadata);
            category.setImagen("https://1023c13-grupo3.s3.amazonaws.com/" + key);
        } catch (IllegalArgumentException | AmazonServiceException e) {
            System.err.println("Error al procesar la imagen " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar la categoría.");
        }
        categoryService.guardar(category);
        return ResponseEntity.status(HttpStatus.OK).body(category.getId());
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUnaCategoria(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return ResponseEntity.ok().body("Se modificó la categoría.");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletecategory(@PathVariable Long id ) throws ResourceNotFoundException {
        categoryService.deletecategory(id);
        return ResponseEntity.ok("Category with id "+ id+" was successfully eliminated. ");
    }
}
