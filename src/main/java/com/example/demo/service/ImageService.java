package com.example.demo.service;

import com.example.demo.entity.Image;
import com.example.demo.entity.Product;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductService productService;

    public Image addImage(Image image){

        Optional<Product> productoBuscado=productService.findProductById(image.getProduct().getId());
        image.setProduct(productoBuscado.get());

        return imageRepository.save(image);
    }

    public List<Image> getAllImages(){
        return  imageRepository.findAll();
    }
    public Optional<Image> findById(Long id){
        return imageRepository.findById(id);
    }
    public Image updateImage(Image image)throws BadRequestException {
        Optional<Image> imagenBuscada = findById(image.getId());
        if (imagenBuscada.isPresent()){
            return imageRepository.save(image);
        }
        else {
            throw new BadRequestException("Could not update image with id : "+image.getId());
        }

    }

    public void deleteImage(Long id) throws ResourceNotFoundException {
        Optional<Image> foundImage= findById(id);
        if (foundImage.isPresent()){
            imageRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("The image with the id: " + id + "does NOT exist");
        }

    }
}
