package com.example.demo.controller;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.demo.entity.Characteristics;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.CharacteristicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/characteristics")
public class CharacteristicsController {
    private final CharacteristicsService characteristicsService;

    public CharacteristicsController(CharacteristicsService characteristicsService) {
        this.characteristicsService = characteristicsService;
    }

    @Autowired
    private AmazonS3 s3;

    @GetMapping("/listar")
    public List<Characteristics> listarcaracteristica() {
        List<Characteristics> characteristics = characteristicsService.showAll();
        return characteristics;
    }

   @GetMapping("/{id}")
    public ResponseEntity<Characteristics> buscarUnaCaracteristica(@PathVariable Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(characteristicsService.buscarPorId(id));
    }

   @PostMapping("/agregar")
    public ResponseEntity<?> agregarCaracteristica(@RequestBody Characteristics characteristics) {
        String base64Image = (characteristics.getIcono() != null) ? characteristics.getIcono() : "";
        ObjectMetadata objectMetadata = new ObjectMetadata();

        try {
            byte[] binaryData = Base64.getDecoder().decode(base64Image);

            String key = characteristics.getTitle() + "_" + UUID.randomUUID().toString() + ".jpg";

            objectMetadata.setContentLength(binaryData.length);
            s3.putObject("1023c13-grupo3", key, new ByteArrayInputStream(binaryData), objectMetadata);
            characteristics.setIcono("https://1023c13-grupo3.s3.amazonaws.com/" + key);
        } catch (IllegalArgumentException | AmazonServiceException e) {
            System.err.println("Error en la imagen " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al agregar la characteristics.");
        }
        characteristicsService.saveCharacteristic(characteristics);
        return ResponseEntity.status(HttpStatus.OK).body(characteristics.getId());
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> actualizarUnaCaracteristica(@RequestBody Characteristics characteristics) {
        characteristicsService.updateCharacteristic(characteristics);
        return ResponseEntity.ok().body("Se modificó la characteristics.");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCaracteristica(@PathVariable Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response = null;
        characteristicsService.deleteCharacteristic(id);
        response = ResponseEntity.status(HttpStatus.OK).body("Caracteristica eliminada.");
        return response;
    }
}