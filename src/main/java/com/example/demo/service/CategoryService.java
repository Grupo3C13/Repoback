package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private static final Logger logger = Logger.getLogger(User.class);

    CategoryRepository categoryRepository;

    private ObjectMapper objectMapper;

    public CategoryService(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    public Long guardar(Category category) {
        logger.info("Categoria guardada");
        categoryRepository.save(category);
        return category.getId();
    }
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) throws ResourceNotFoundException {
        Optional<Category> found = categoryRepository.findById(id);
        if (found.isPresent()) {
            logger.info("Se encontro la categoria");
            return found.get();
        } else {
            logger.warn("No se encontro ninguna categoria con ese ID");
            throw new ResourceNotFoundException("La categoria no existe");
        }
    }

        public void updateCategory(Category categoria) {
             logger.info("Se actualiza la categoria");
             guardar(categoria);
}
        public void deletecategory(Long id) throws ResourceNotFoundException {
        Optional<Category> found = categoryRepository.findById(id);
        if (found.isPresent()) {
            categoryRepository.deleteById(id);
            logger.warn("Se ha eliminado la categoria");
        } else {
            logger.error("No se ha encontrado ninguna categoria con id " + id);
            throw new ResourceNotFoundException("No se encuentra la categoria");
        }
    }
}
