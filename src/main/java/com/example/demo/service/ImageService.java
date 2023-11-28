package com.example.demo.service;

import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ImageRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {


    private static final Logger logger = Logger.getLogger(User.class);

    ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Long saveImage(Image image) {
        logger.info("Image - guardar: Se va a guardar la imagen");
        imageRepository.save(image);
        return image.getId();
    }

    public void modificar(Image image) {
        logger.info("Image - actualizar: Se va a actualizar la imagen");
        saveImage(image);
    }


    public void eliminar(Long id) throws ResourceNotFoundException {
        Optional<Image> found = imageRepository.findById(id);
        if (found.isPresent()) {
            imageRepository.deleteById(id);
            logger.warn("Image - eliminar: Se ha eliminado la imagen");
        } else {
            logger.error("No se ha encontrado ninguna imagen con id " + id);
            throw new ResourceNotFoundException("No se ha encontrado la imagen");
        }
    }


    public void guardarTodas(List<Image> images) {
        imageRepository.saveAll(images);
    }
}
