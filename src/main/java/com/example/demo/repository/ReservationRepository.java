package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query ("SELECT r FROM Reservation r WHERE  r.user.id = :userId")
    List<Reservation> searchReservationByUserId(@Param("userId") Long userId);

    @Query ("SELECT r FROM Reservation r WHERE  r.product.id = :productId")
    List<Reservation> searchReservationByProductId(@Param("productId") Long productId);


}
