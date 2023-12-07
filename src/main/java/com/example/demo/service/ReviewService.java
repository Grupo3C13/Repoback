package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private static final Logger logger = Logger.getLogger(Review.class);
    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    private final UserService userServicee;
    private ObjectMapper objectMapper;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ProductService productService, UserService userServicee, ObjectMapper objectMapper) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.userServicee = userServicee;
        this.objectMapper = objectMapper;
    }

    public Long addReview(Review review) throws ResourceNotFoundException {
        logger.info("Reseña - guardar: Se va a guardar la reseña");
        objectMapper.registerModule(new JavaTimeModule());
        Long productId = review.getProduct().getId();
        Long userId= review.getUser().getId();
        UserDTO u = userServicee.buscarPorId(userId);
        ProductDTO p = productService.searchById(productId);
        User user = objectMapper.convertValue(u,User.class);
        Product product = objectMapper.convertValue(p, Product.class);

        if(product.getCantReviews()==null){
            product.setScore(0d);
            product.setCantReviews(0);
        }
        product.setScore(((product.getScore()*product.getCantReviews())+review.getScore())/(product.getCantReviews()+1));
        product.setCantReviews(product.getCantReviews()+1);

        review.setProduct(product);
        review.setUser(user);
        if(product.getReviews()==null){
            List<Review> listaNueva = new ArrayList<>();
            listaNueva.add(review);
            product.setReviews(listaNueva);
        }else {
            List<Review> listaResenias = product.getReviews();
            listaResenias.add(review);
            product.setReviews(listaResenias);
        }


        productService.update(product);
        reviewRepository.save(review);
        return review.getId();
    }

    public List<Review> findAllReviews(){
        return reviewRepository.findAll();
    }

    public List<Review> findReviewByProductId(Long id){
        return reviewRepository.findAllByProductId(id);
    }


    public Optional<Review> findReviewById(Long id){
        return reviewRepository.findById(id);
    }

    public void update(Review review) throws ResourceNotFoundException {
        logger.info("Reseña - actualizar: se va a actualizar la reseña");
        addReview(review);
    }
    public void delete(Long id) throws ResourceNotFoundException{
        Optional<Review> found = reviewRepository.findById(id);
        if(found.isPresent()){
            reviewRepository.deleteById(id);
            logger.warn("Se ha eliminado la review");
        }else{
            logger.warn("No se ha encontrado ninguna reseña con id "+ id);
            throw new ResourceNotFoundException("No se ha encontrado la review");
        }
    }
    public List<Review> buscarReseniaPorProductId(Long id) throws ResourceNotFoundException{
        List<Review> lista = reviewRepository.findAllByProductId(id);
        if(lista!=null){
            return lista;
        }
        throw new ResourceNotFoundException("No existen reseñas");

    }
}
