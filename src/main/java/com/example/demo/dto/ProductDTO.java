package com.example.demo.dto;

import com.example.demo.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private Double score;
    private Integer cantReviews;
    private Double price;
    private String imgUrl;
    private List<String> listImgUrl;
    private List<Category> categories;
    private List<Characteristics> characteristics;
    private List<String> imagesBase64;
    private List<Review> reviews;
    private List<Reservation> rentedByUsers;

}
