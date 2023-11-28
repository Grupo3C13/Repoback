package com.example.demo.controller;

import com.example.demo.entity.Reservation;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    @GetMapping("/listar")
    public List<Reservation> buscarReservations() {
        List<Reservation> listarReservations = reservationService.mostrarTodos();
        for(Reservation r : listarReservations) {
            System.out.println(r.getStartDate()+r.getUser().getName());
        }
        System.out.println("Saliendo listar reservationController");
        return listarReservations;
    }


    @GetMapping("/{id}")
    public ResponseEntity<Reservation> buscarReservation(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(reservationService.buscarPorId(id));
    }

     @PostMapping("/agregar")
    public ResponseEntity<?> agregarReserva(@RequestBody Reservation reservation) {
        try {
            Long reservationId = reservationService.guardar(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservationId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al agregar la reserva.");
        }
    }

    @PutMapping("/modificar")
    public ResponseEntity<?> actualizarReservation(@RequestBody Reservation reservation) {
        try {
            reservationService.modificar(reservation);
            return ResponseEntity.ok().body("Se modificó la reserva.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la reserva para modificar.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al modificar la reserva.");
        }
    }
    

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarReservation(@PathVariable Long id) {
        try {
            reservationService.eliminar(id);
            return ResponseEntity.status(HttpStatus.OK).body("Reserva eliminada.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró la reserva para eliminar.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error al eliminar la reserva.");
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<Reservation>> buscarReservaPorUserId(@PathVariable Long id) {
        try {
            List<Reservation> lista = reservationService.buscarReservaPorUserId(id);
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Reservation>> buscarReservaPorProductId(@PathVariable Long id) {
        try {
            List<Reservation> lista = reservationService.buscarReservaPorProductId(id);
            return ResponseEntity.status(HttpStatus.OK).body(lista);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
