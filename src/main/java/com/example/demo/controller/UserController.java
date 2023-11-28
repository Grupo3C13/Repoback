package com.example.demo.controller;


import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:8090")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/listar")
    public List<UserDTO> buscarUsers() {
        List<UserDTO> listarUsers = userService.mostrarTodos();
        return listarUsers;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarUnUser(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(userService.buscarPorId(id));
    }
    @PostMapping("/agregar")
    public ResponseEntity<?> agregarUser(@RequestBody User user) {
        userService.guardar(user);
        return ResponseEntity.status(HttpStatus.OK).body(user.getId());
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> actualizarUnUser(@RequestBody User user) {
        userService.modificar(user);
        return ResponseEntity.ok().body("Se modifico el usuario.");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarProduct( @PathVariable Long id) throws ResourceNotFoundException {
        ResponseEntity<?> response = null;
        userService.eliminar(id);
        response = ResponseEntity.status(HttpStatus.OK).body("Usuario eliminado.");
        return response;
    }

    @GetMapping("/perfil/{email}")
    public ResponseEntity<UserDTO> buscarPorEmail(@PathVariable String email)throws ResourceNotFoundException{
        ResponseEntity<?> response = null;
        UserDTO user = userService.buscarPorEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/mostrarFav/{id}")
    public List<Product> mostrarFavoritos(@PathVariable Long id) throws ResourceNotFoundException{
        List<Product> lista = null;
        lista = userService.listarFavoritos(id);
        if(lista!=null){
            return lista;
        }else {
            throw new ResourceNotFoundException("No se encontraron favoritos.");
        }
    }

    @PostMapping("/{userId}/agregarFav/{productId}")
    public ResponseEntity<?> agregarFavorito(@PathVariable Long userId, @PathVariable Long productId)throws ResourceNotFoundException {
        userService.guardarFavorito(userId, productId);
        ResponseEntity<?> response = null;
        response = ResponseEntity.status(HttpStatus.OK).body("Producto agregado a favoritos con exito");
        return response;
    }

    @DeleteMapping("/{userId}/eliminarFav/{productId}")
    public ResponseEntity<?> eliminarFavorito(@PathVariable Long userId, @PathVariable Long productId)throws ResourceNotFoundException {
        userService.eliminarFavorito(userId, productId);
        ResponseEntity<?> response = null;
        response = ResponseEntity.status(HttpStatus.OK).body("Producto eliminado de favoritos con exito");
        return response;
    }

}
