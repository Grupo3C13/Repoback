package com.example.demo.dto;


import com.example.demo.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResume {
    private Long id;
    private String name;
    private Double price;
    private String imgUrl;
    private Double score;
    private Integer cantReviews;
    private List<Category> categories;

    public ProductResume(Long id, String name, Double price, Double score, Integer cantReviews) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.score=score;
        this.cantReviews=cantReviews;
    }
}
