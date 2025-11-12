package com.JFBRA.repository;

import com.JFBRA.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByContactEmail(String contactEmail);

    Optional<Reservation> findByBoothId(Long boothId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.booth.id = :boothId " +
            "AND r.agreedToTerms = true " +
            "AND r.agreedToParticipation = true " +
            "AND r.agreedToConditions = true")
    Optional<Reservation> findActiveReservationByBoothId(@Param("boothId") Long boothId);

    boolean existsByContactEmail(String contactEmail);
}