package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ProductResume;
import com.example.demo.entity.*;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class ProductService {

    private static final Logger logger = Logger.getLogger(ProductService.class);

    private final ProductRepository productRepository;
    private final CharacteristicsService characteristicsService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PolicyService policyService;
    private final ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository, CharacteristicsService characteristicsService, CategoryService categoryService, UserRepository userRepository, UserService userService, PolicyService policyService, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.characteristicsService = characteristicsService;
        this.categoryService = categoryService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.policyService = policyService;
        this.objectMapper = objectMapper;
    }

    public Long addProduct(Product product) throws ResourceNotFoundException {
        logger.info("Libros - guardar: Se va a guardar el libro");
        if(product.getCantReviews()==null){
            product.setCantReviews(0);
            product.setScore(0d);
        }
        Long id = productRepository.save(product).getId();
        return product.getId();
    }

    public List<ProductDTO> showAll() throws ResourceNotFoundException {
        objectMapper.registerModule(new JavaTimeModule());
        List<ProductDTO> productDTOS = productRepository.findAll()
                .stream()
                .map(product -> {
                    try {
                        Long id = product.getId();
                        List<String> images = buscarListaImagenes(id);
                        List<Category> categories = buscarCategoria(id);
                        List<Characteristics> characteristics=buscarCaracteristica(id);
                        if (images == null) {
                            throw new ResourceNotFoundException("Imagenes no encontradas.");
                        }
                        ProductDTO productDTO = objectMapper.convertValue(product, ProductDTO.class);
                        productDTO.setCategories(categories);
                        productDTO.setCharacteristics(characteristics);
                        return productDTO;
                    } catch (ResourceNotFoundException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return productDTOS;
    }

    public ProductDTO searchById(Long id) throws ResourceNotFoundException {
        objectMapper.registerModule(new JavaTimeModule());
        Optional<Product> found = productRepository.findById(id);
        if (found.isPresent()) {
            Product p = found.get();
            List<String> images = buscarListaImagenes(id);
            List<Category> categorias = buscarCategoria(id);
            List<Characteristics> characteristics=buscarCaracteristica(id);
            ProductDTO productDTO = objectMapper.convertValue(found, ProductDTO.class);
            productDTO.setListImgUrl(images);
            productDTO.setCategories(categorias);
            productDTO.setCharacteristics(characteristics);
            return productDTO;
        } else {
            throw new ResourceNotFoundException("El libro no existe");
        }
    }

    public List<String> buscarListaImagenes(Long id) throws ResourceNotFoundException {
        List<String> lista = productRepository.searchImage(id);
        if (lista != null) {
            return lista;
        } else {
            throw new ResourceNotFoundException("No se encontraron imagenes para el libro con id: " + id);
        }
    }
    public List<Category> buscarCategoria(Long id) throws ResourceNotFoundException{
        List<Category> lista = productRepository.searchCategoryById(id);
        if(lista!=null){
            return lista;
        }else{
            throw new ResourceNotFoundException("No se encontraron categorias.");
        }
    }
    public List<Characteristics> buscarCaracteristica(Long id) throws ResourceNotFoundException{
        List<Characteristics> lista = productRepository.searchCharacteristicsById(id);
        if(lista!=null){
            return lista;
        }else{
            throw new ResourceNotFoundException("No se encontraron categorias.");
        }
    }
    public Product buscarPorId2(Long id) throws  ResourceNotFoundException {
        Optional<Product> found = productRepository.findById(id);
        if (found.isPresent()) {
            Product p = found.get();
            List<Category> categories = buscarCategoria(id);
            List<Characteristics> characteristics = buscarCaracteristica(id);
            p.setCategories(categories);
            p.setCharacteristics(characteristics);
            return p;
        } else {
            logger.warn("No se encontro ninguno con ese ID");
            throw new ResourceNotFoundException("No existe");
        }
    }
    public void guardarCategoria(Long productId, Long categoriesId) throws ResourceNotFoundException{
        Product p = this.buscarPorId2(productId);
        Category c = categoryService.getCategoryById(categoriesId);
        List<Category> lista = p.getCategories();
        lista.add(c);
        p.setCategories(lista);
        this.addProduct(p);
    }
    public void guardarCaracteristica(Long productId, Long caracteristicaId) throws ResourceNotFoundException{
        Product p = this.buscarPorId2(productId);
        Characteristics c = characteristicsService.buscarPorId(caracteristicaId);
        List<Characteristics> lista = p.getCharacteristics();
        lista.add(c);
        p.setCharacteristics(lista);
        this.addProduct(p);
    }

    public void update(Product product) throws ResourceNotFoundException {
        logger.info("Se actualiza el producto");
        addProduct(product);
    }
    public List<ProductResume> listEverything() throws ResourceNotFoundException{
        List<ProductResume> lista = productRepository.findProductResume();
        for(ProductResume b : lista){
            b.setCategories(productRepository.searchCategoryById(b.getId()));
            List<String> listaImagenes = productRepository.searchImage(b.getId());
            String imagen = listaImagenes.stream().findFirst().orElse(null);
            b.setImgUrl(imagen);
        }
        return lista;
    }

    public void delete(Long id) throws ResourceNotFoundException {
        System.out.println("DENTRO DE ELIMINAR LIBRO");
        Optional<Product> found = productRepository.findById(id);
        if (found.isPresent()) {
            deleteAllFavs(id);
            logger.warn("Se ha eliminado el libro");
        } else {
            logger.error("No se ha encontrado con id " + id);
            throw new ResourceNotFoundException("No se ha encontrado");
        }
    }

    public void deleteAllFavs(Long productId) throws ResourceNotFoundException {
        System.out.println("eliminando");
        List<User> listUser = userRepository.searchProductsFavs(productId);

        for(User u : listUser){
            System.out.println("USUARIO");

            userService.eliminarFavorito(u.getId(),productId);
        }
        userRepository.deleteById(productId);
    }

}
