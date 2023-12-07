package com.example.demo.controller;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductResume;
import com.example.demo.entity.Category;
import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.CategoryService;
import com.example.demo.service.CharacteristicsService;
import com.example.demo.service.ImageService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CharacteristicsService characteristicsService;

    @Autowired
    private AmazonS3 s3;

    public ProductController(ProductService productService, ImageService imageService, CategoryService categoryService, CharacteristicsService characteristicsService) {
        this.productService = productService;
        this.imageService = imageService;
        this.categoryService = categoryService;
        this.characteristicsService = characteristicsService;
    }

    @GetMapping("/listar")
    public List<ProductDTO> searchProducts() throws Exception {
        List<ProductDTO> listProducts = productService.showAll();
        return listProducts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(productService.searchById(id));
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<?> agregar(@RequestBody Product product) throws ResourceNotFoundException{
        String name = product.getName();
        String description = product.getDescription();
        List<Category> lista = product.getCategories();

        List<String> base64Images = (product.getImagesBase64() != null) ? product.getImagesBase64() : Collections.emptyList();

        List<Image> images = new ArrayList<>();

        ObjectMetadata objectMetadata = new ObjectMetadata();

        Product newProduct = product;

        Long id = productService.addProduct(newProduct);
        newProduct.setId(id);
        newProduct.setCategories(lista);

        for (String base64Image : base64Images) {
            System.out.println("Base64 Image: " + base64Image);

            byte[] binaryData = Base64.getDecoder().decode(base64Image);

            name=name.replace(" ","_");
            String key = name + "_" + UUID.randomUUID().toString() + ".jpg";

            try {
                objectMetadata.setContentLength(binaryData.length);

                // Subir la imagen a S3
                s3.putObject("1023c13-grupo3", key, new ByteArrayInputStream(binaryData), objectMetadata);

                // Crear una nueva entidad Image con la URL de la imagen
                Image image = new Image();
                image.setUrl("https://1023c13-grupo3.s3.amazonaws.com/" + key);
                image.setProduct(newProduct);
                imageService.guardar(image);
                images.add(image);


            } catch (IllegalArgumentException | AmazonServiceException e) {
                System.err.println("Error al procesar la imagen " + e.getMessage());
                // Manejar el error apropiadamente (puede ser útil lograrlo o devolver un mensaje más detallado)
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar producto.");
            }
        }
        newProduct.setImagesBase64(null);
        newProduct.setImages(images);
        newProduct.setImgUrl(newProduct.getImages().get(0).getUrl());
        productService.addProduct(newProduct);


        return ResponseEntity.status(HttpStatus.OK).body(product.getId());
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> updateProduct(@RequestBody Product product) throws ResourceNotFoundException{
        productService.update(product);
        return ResponseEntity.ok().body("Se modifico.");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response = null;
        productService.delete(id);
        response = ResponseEntity.status(HttpStatus.OK).body("Producto eliminado.");
        return response;
    }
    @PutMapping("/{productsId}/categories/{categoriesId}")
    public ResponseEntity<?> agregarCategoria(@PathVariable Long productsId, @PathVariable Long categoriesId) throws ResourceNotFoundException{
        ResponseEntity<?> response=null;
        productService.guardarCategoria(productsId,categoriesId);
        return  ResponseEntity.ok().body("Categoria agregada");
    }

    @PutMapping("/{productsId}/characteristics/{characteristicId}")
    public ResponseEntity<?> agregarCaracteristica(@PathVariable Long productsId, @PathVariable Long characteristicId) throws ResourceNotFoundException{
        ResponseEntity<?> response=null;
        productService.guardarCaracteristica(productsId,characteristicId);
        return response;
    }

    @GetMapping("/listeverything")
    public List<ProductResume> listEverything(){
        List<ProductResume> lista = null;
        try{
            lista =productService.listEverything();
        }catch (ResourceNotFoundException e){
            System.out.println(e);
        }
        return lista;
    }
}
