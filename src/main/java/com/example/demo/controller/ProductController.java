package com.example.demo.controller;


import com.example.demo.entity.Product;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;


    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        //List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(productService.getAllProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(@PathVariable Long id) {
        Optional<Product> productoBuscado = productService.findProductById(id);
        return productoBuscado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<List<Product>> findAllByCategory(@PathVariable Long id) {
        Optional<List<Product>> productosBuscados = productService.findAllByCategory(id);
        return productosBuscados.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
   }

    @PutMapping
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) throws BadRequestException {
        Product editedProduct=productService.updateProduct(product);
        return ResponseEntity.ok(editedProduct);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePdoductById(@PathVariable Long id) throws ResourceNotFoundException{
        productService.deletePdoductById(id);
        return ResponseEntity.ok("Product with id " + id + " was deleted. ");
    }
}
