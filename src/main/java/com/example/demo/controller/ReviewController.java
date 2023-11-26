package com.example.demo.controller;

import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> addReview(@RequestBody Review review){
        return ResponseEntity.ok(reviewService.addReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(){
        return ResponseEntity.ok(reviewService.findAllReviews());
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Review>> findReviewByProduct(@PathVariable Long id){
        Optional<List<Review>> reviews = reviewService.findReviewByProductId(id);
        return reviews.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


}
