package com.example.demo.repository;

import com.example.demo.dto.ProductResume;
import com.example.demo.entity.Category;
import com.example.demo.entity.Characteristics;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
//    Optional<List<Product>> findAllByCategory_Id(Long categoryId);

    @Query("SELECT i.url FROM Image i WHERE i.product.id = ?1")
    List<String> searchImage (Long id);

    @Query("SELECT p.categories FROM Product p WHERE p.id = :productId")
    List<Category> searchCategoryById(@Param("productId") Long productId);

    @Query("SELECT p.characteristics FROM Product p WHERE p.id = :productId")
    List<Characteristics> searchCharacteristicsById(@Param("productId") Long productId);

    @Query("SELECT new com.example.demo.dto.ProductResume(p.id, p.name, p.price, p.imgUrl, p.score, p.cantReviews) FROM Product p ")
    List<ProductResume> findProductResume();
}
