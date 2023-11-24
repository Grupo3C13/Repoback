package com.example.demo.service;


import com.example.demo.entity.AppUser;
import com.example.demo.entity.Product;
import com.example.demo.entity.Score;
import com.example.demo.exceptions.BadRequestException;
import com.example.demo.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScoreService {

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ProductService productService;

    @Autowired
    UserService usuarioService;

    public Optional<Score> findScoreById(Long id){
        return scoreRepository.findById(id);
    }

//    public Optional <List<Score>> findScoreByProductId(Long id) {
//        return scoreRepository.findAllByProductoId(id);
//    }

    public Score addScore(Score puntuacion){

        Optional<Product> productoBuscado=productService.findProductById(puntuacion.getProduct().getId());
        Optional<AppUser> usuarioBuscado=usuarioService.findUserById(puntuacion.getUser().getId());
        puntuacion.setProduct(productoBuscado.get());
        puntuacion.setUser(usuarioBuscado.get());
        return scoreRepository.save(puntuacion);
    }

    public Score editarPuntuacion(Score puntuacion)throws BadRequestException {
        //se le añaden algunas verificaciones para que realmente se pueda modificar (debe tener elementos de un puntaje existente)
        Optional<Score> puntuacionBuscada = findScoreById(puntuacion.getId());
        Optional<Product> productoBuscado= productService.findProductById(puntuacion.getProduct().getId());
        if (productoBuscado.isPresent()&&puntuacionBuscada.isPresent()){
            return scoreRepository.save(puntuacion);
        }else{
            throw new BadRequestException("No se puede actualizar la puntuacion con id :"+ puntuacion.getId());
        }

    }

}
