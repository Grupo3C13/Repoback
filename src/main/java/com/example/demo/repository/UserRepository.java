package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query("SELECT u.productsFavs FROM User u WHERE u.id = :userId")
    List<Product> buscarFavoritos(@Param("userId") Long userId);
//    List<User> searchProductsFavs(@Param("productId") Long bookId);
}
