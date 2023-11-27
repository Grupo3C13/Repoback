package com.example.demo.service;

import com.example.demo.entity.Characteristics;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.CharacteristicsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacteristicsService {
    private static final Logger logger = Logger.getLogger(User.class);

    // Repositorio de User utilizado para acceder a la base de datos.
    private final CharacteristicsRepository characteristicsRepository;

    // Para la conversión de objetos.
    private ObjectMapper objectMapper;

    // Constructor de SubscriptionService que permite la inyección de dependencias.


    public CharacteristicsService(CharacteristicsRepository characteristicsRepository, ObjectMapper objectMapper) {
        this.characteristicsRepository = characteristicsRepository;
        this.objectMapper = objectMapper;
    }


    public Long saveCharacteristic(Characteristics characteristics) {
        logger.info("Caracteristica - guardar: Se va a guardar la característica");
        characteristicsRepository.save(characteristics);
        return characteristics.getId();
    }


    public List<Characteristics> showAll() {
        return characteristicsRepository.findAll();
    }

    public Characteristics buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Characteristics> found = characteristicsRepository.findById(id);
        if (found.isPresent()) {
            logger.info("Se encontró la característica y se devolverá");
            return found.get();
        } else {
            logger.warn("No se encontró ninguna característica con ese ID");
            throw new ResourceNotFoundException("No existe");
        }
    }

    public void updateCharacteristic(Characteristics characteristics) {
        logger.info("Caracteristica - actualizar: Se va a actualizar la característica");
        saveCharacteristic(characteristics);
    }

    public void deleteCharacteristic(Long id) throws ResourceNotFoundException {
        Optional<Characteristics> found = characteristicsRepository.findById(id);
        if (found.isPresent()) {
            characteristicsRepository.deleteById(id);
            logger.warn("Se ha eliminado la característica");
        } else {
            logger.error("No se ha encontrado ninguna característica con id " + id);
            throw new ResourceNotFoundException("No se encontro la característica");
        }
    }
}
