package com.example.demo.service;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService  {

    private static final Logger logger = Logger.getLogger(User.class);
    UserRepository userRepository;
    private ProductRepository productRepository;

    private ObjectMapper objectMapper;
    // Constructor de SubscriptionService que permite la inyección de dependencias.


    public UserService(UserRepository userRepository, ProductRepository productRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    public Long guardar(User user) {
        logger.info("User - guardar: Se va a guardar el usuario");
        userRepository.save(user);
        return user.getId();
    }


    public List<UserDTO> mostrarTodos() {
        objectMapper.registerModule(new JavaTimeModule()); // Se utiliza para solucionar el error "not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310""
        List<UserDTO> UserDTO = new ArrayList<>();  // Creamos un ArrayList de tipo UserDTO
        for (User p : userRepository.findAll()){    // Iteramos el array
            logger.info("e esta iterando el array de usuarios");
            UserDTO.add(objectMapper.convertValue(p,UserDTO.class));  // En cada iteración convertimos el objeto de tipo User a UserDTO y lo agregamos al ArrayList
        }
        return UserDTO;
    }

    public UserDTO buscarPorId(Long id) throws ResourceNotFoundException {
        objectMapper.registerModule(new JavaTimeModule()); // Se utiliza para solucionar el error "not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jsr310""
        Optional<User> found = userRepository.findById(id);  // Utilizo el objeto Optional que permite que "found" devuelva nulo o User
        if(found.isPresent()) {  // Evaluamos si found tiene contenido
            logger.info("User - buscarPorId: Se encontro el usuario y se convertira a DTO para ser devuelto");
            return objectMapper.convertValue(found, UserDTO.class);  // Convertimos a found que es de tipo User a UserDTO.
        } else {
            logger.warn("User - buscarPorId: No se encontro ningun usuario con ese ID");
            throw new ResourceNotFoundException("El User no existe");
        }
    }


    public void modificar(User user) {
        logger.info("User - actualizar: Se va a actualizar  el usuario");
        guardar(user); // El método utiliza .save; este lo que hace es crear si el ID = 0 pero si ID!=0 actualiza los cambios.
    }

    public void eliminar(Long id) throws ResourceNotFoundException {
        Optional<User> found = userRepository.findById(id);
        if(found.isPresent()){
            userRepository.deleteById(id);
            logger.warn("User - eliminar: Se ha eliminado el usuario");
        } else {
            logger.error("No se ha encontrado ningun usuario con id " + id);
            throw new ResourceNotFoundException("No se ha encontrado el usuario");
        }
    }

    public UserDTO buscarPorEmail(String email)throws ResourceNotFoundException{
        Optional<User> found = userRepository.findByEmail(email);
        objectMapper.registerModule(new JavaTimeModule());
        if(found.isPresent()){
            return objectMapper.convertValue(found, UserDTO.class);
        }else{
            logger.error("No se ha encontrado ningun usuario con email " + email);
            throw new ResourceNotFoundException("No se ha encontrado el usuario");
        }
    }

    public List<Product> listarFavoritos(Long id) throws ResourceNotFoundException{
        System.out.println("Buscando Favorito");
        List<Product> lista = userRepository.searchFavs(id);
        if(lista != null){
            return lista;
        }else{
            logger.error("No se ha encontrado ninguno  " + id);
            throw new ResourceNotFoundException("No se encontraron favoritos: "+ id);
        }
    }

    public void guardarFavorito(Long userId, Long productId) throws ResourceNotFoundException{
        Optional<User> u = userRepository.findById(userId);
        Optional<Product> p = productRepository.findById(productId);
        List<Product> listaFav = null;
        if(u.isPresent()&& p.isPresent()){
            listaFav= u.get().getProductsFavs();
            listaFav.add(p.get());
            u.get().setProductsFavs(listaFav);
            guardar(u.get());
        }else {
            logger.error("No se pudo agregar a favoritos.");
            throw new ResourceNotFoundException("No se pudo agregar a favoritos.");
        }
    }

    public void eliminarFavorito(Long userId, Long productId) throws ResourceNotFoundException{
        Optional<User> u = userRepository.findById(userId);
        Optional<Product> p = productRepository.findById(productId);

        List<Product> listaFav = null;
        if(u.isPresent()&& p.isPresent()){
            listaFav= u.get().getProductsFavs();
            listaFav.remove(p.get());
            u.get().setProductsFavs(listaFav);
            guardar(u.get());
        }else {
            logger.error("No se pudo eliminar de favoritos.");
            throw new ResourceNotFoundException("No se pudo eliminar de favoritos.");
        }
    }


    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

}
