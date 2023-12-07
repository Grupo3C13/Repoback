package com.example.demo.controller;

import com.example.demo.entity.Review;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> agregarResenia(@RequestBody Review review) throws ResourceNotFoundException {
        reviewService.addReview(review);
        return ResponseEntity.status(HttpStatus.OK).body(review.getId());
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(){
        return ResponseEntity.ok(reviewService.findAllReviews());
    }

    @GetMapping("/product/{id}")
    public List<Review> findReviewByProduct(@PathVariable Long id)throws ResourceNotFoundException{
        List<Review> lista = reviewService.findReviewByProductId(id);
        if(lista!=null){
            return lista;
        }else {
            throw new ResourceNotFoundException("No se encontraron reseñas para el libero.");
        }
    }


}
