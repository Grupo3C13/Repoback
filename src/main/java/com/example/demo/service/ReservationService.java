package com.example.demo.service;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Product;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.User;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repository.ReservationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class ReservationService {

    private static final Logger logger = Logger.getLogger(Reservation.class);

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ReservationService(ReservationRepository reservationRepository, UserService userService,
                              ProductService productService, ObjectMapper objectMapper) {
    this.reservationRepository = reservationRepository;
    this.userService = userService;
    this.productService = productService;
    this.objectMapper = objectMapper;
}





    public Long guardar(Reservation reservation) throws ResourceNotFoundException {
        Long userId = reservation.getUser().getId();
        Long productId = reservation.getProduct().getId();
        UserDTO u = userService.buscarPorId(userId);
        ProductDTO p = productService.searchById(productId);
        objectMapper.registerModule(new JavaTimeModule());
        User user = objectMapper.convertValue(u,User.class);
        Product product = objectMapper.convertValue(p, Product.class);
        reservation.setProduct(product);
        reservation.setUser(user);

        LocalDate fechaInicio =reservation.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFin = reservation.getReturnDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        fechaInicio=fechaInicio.plusDays(1);
        fechaFin=fechaFin.plusDays(1);
        Date inicioFormateado = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date finFormateado = Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

        reservation.setStartDate(inicioFormateado);
        reservation.setReturnDate(finFormateado);

        reservationRepository.save(reservation);
        return reservation.getId();
    }
    
    public List<Reservation> mostrarTodos() {
        List<Reservation> reservations = new ArrayList<>();
        for (Reservation reservation : reservationRepository.findAll()) {
            logger.info("Reserva - mostrarTodos: Iterando reservas");
            reservations.add(reservation);
            logger.info("Producto: " + reservation.getProduct().getDescription());
            logger.info("Usuario: " + reservation.getUser().getName());
        }
        return reservations;
    }

 
    public Reservation buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Reservation> found = reservationRepository.findById(id);
        if (found.isPresent()) {
            logger.info("Reserva - buscarPorId: Se encontró la reserva");
            return found.get();
        } else {
            logger.warn("Reserva - buscarPorId: No se encontró ninguna reserva con ese ID");
            throw new ResourceNotFoundException("La reserva no existe");
        }
    }

   
    public void modificar(Reservation reservation) throws ResourceNotFoundException {
        logger.info("Reserva - actualizar: Se va a actualizar la reserva");
        guardar(reservation); // El método utiliza .save; este lo que hace es crear si el ID = 0 pero si ID!=0 actualiza los cambios.
    }

  
  
    public void eliminar(Long id) throws ResourceNotFoundException {
    try {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            logger.warn("Se ha eliminado la reserva con ID " + id);
        } else {
            logger.error("No se encuentra la reserva con ID " + id);
            throw new ResourceNotFoundException("No se ha encontrado");
        }
    } catch (EmptyResultDataAccessException e) {
        logger.error("Error al eliminar la reserva " + id + ": " + e.getMessage());
        throw new ResourceNotFoundException("No se ha encontrado la reserva");
    }
}

    public List<Reservation> buscarReservaPorUserId(Long userId) throws ResourceNotFoundException {
        return reservationRepository.searchReservationByUserId(userId);
    }

    public List<Reservation> buscarReservaPorProductId(Long productId) throws ResourceNotFoundException {
        return reservationRepository.searchReservationByProductId(productId);
    }

}
