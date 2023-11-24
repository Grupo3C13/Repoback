package com.example.demo.controller;


import com.example.demo.entity.Score;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

//    @GetMapping("/product/{id}")
//    public ResponseEntity<List<Score>> findScoreByProductId(@PathVariable Long id)  {
//        Optional<List<Score>> puntuacionesBuscadas=scoreService.findScoreByProductId(id);
//        return puntuacionesBuscadas.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
    @PostMapping
    public ResponseEntity<Score> addScore(@RequestBody Score puntuacion) {
        return ResponseEntity.ok(scoreService.addScore(puntuacion));
    }

    @PutMapping
    public ResponseEntity<Score> updateScore(@RequestBody Score puntuacion) throws BadRequestException {
        Score puntuacionEditada=scoreService.editarPuntuacion(puntuacion);
        return ResponseEntity.ok(puntuacionEditada);
    }

}
