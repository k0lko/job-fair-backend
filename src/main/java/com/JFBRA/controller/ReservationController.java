package com.JFBRA.controller;

import com.JFBRA.dto.ReservationDto;
import com.JFBRA.model.Reservation;
import com.JFBRA.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationDto reservationDto) {
        try {
            Reservation reservation = reservationService.createReservation(reservationDto);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Reservation>> getReservationsByEmail(@PathVariable String email) {
        List<Reservation> reservations = reservationService.getReservationsByEmail(email);
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Reservation reservation = reservationService.getReservationById(id);
        if (reservation != null) {
            return ResponseEntity.ok(reservation);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/my")
    public ResponseEntity<List<Reservation>> getMyReservations(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String email = principal.getName();
        List<Reservation> reservations = reservationService.getReservationsByEmail(email);
        return ResponseEntity.ok(reservations);
    }
}