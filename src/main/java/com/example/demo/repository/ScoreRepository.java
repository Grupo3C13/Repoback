package com.example.demo.repository;

import com.example.demo.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {

    //Optional<List<Score>> findAllByProductoId(Long productIS);
}

