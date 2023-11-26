package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userServicee;

    public Review addReview (Review review){
        Optional<Product> productOptional = productService.findProductById(review.getProduct().getId());
        Optional<User> userOptional= userServicee.findUserById(review.getUser().getId());
        review.setProduct(productOptional.get());
        review.setUser(userOptional.get());
        return reviewRepository.save(review);
    }

    public List<Review> findAllReviews(){
        return reviewRepository.findAll();
    }

    public Optional<List<Review>> findReviewByProductId(Long id){
        return reviewRepository.findAllByProductId(id);
    }


    public Optional<Review> findReviewById(Long id){
        return reviewRepository.findById(id);
    }

}
