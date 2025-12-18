package com.JFBRA.service;

import com.JFBRA.dto.ReservationDto;
import com.JFBRA.model.AdditionalService;
import com.JFBRA.model.Booth;
import com.JFBRA.model.Reservation;
import com.JFBRA.repository.BoothRepository;
import com.JFBRA.repository.ReservationRepository;
import com.JFBRA.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@org.springframework.stereotype.Service // <-- UWAGA: pełna ścieżka
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ServiceRepository serviceRepository;
    private final BoothRepository boothRepository;

    // ===============================
    // CREATE RESERVATION
    // ===============================
    @Transactional
    public Reservation createReservation(ReservationDto reservationDto) {

        Booth booth = boothRepository.findById(reservationDto.getBoothId())
                .orElseThrow(() ->
                        new RuntimeException("Nie znaleziono stoiska o ID: " + reservationDto.getBoothId())
                );

        if (booth.getStatus() != Booth.BoothStatus.AVAILABLE) {
            throw new RuntimeException("To stoisko nie jest dostępne do rezerwacji");
        }

        if (reservationRepository.existsByContactEmail(reservationDto.getContactEmail())) {
            throw new RuntimeException("Ten adres email ma już istniejącą rezerwację");
        }

        // Usługi po serviceCode
        List<AdditionalService> additionalservices =
                serviceRepository.findByServiceCodeIn(reservationDto.getServices());

        int servicesTotal = additionalservices.stream()
                .mapToInt(AdditionalService::getPrice)
                .sum();

        int totalAmount = booth.getPrice() + servicesTotal;

        Reservation reservation = Reservation.builder()
                .booth(booth)
                .companyName(reservationDto.getCompanyName())
                .industry(reservationDto.getIndustry())
                .website(reservationDto.getWebsite())
                .contactName(reservationDto.getContactName())
                .contactEmail(reservationDto.getContactEmail())
                .contactPhone(reservationDto.getContactPhone())
                .invoiceAddress(
                        Reservation.InvoiceAddress.builder()
                                .companyName(reservationDto.getInvoiceAddress().getCompanyName())
                                .street(reservationDto.getInvoiceAddress().getStreet())
                                .postalCode(reservationDto.getInvoiceAddress().getPostalCode())
                                .city(reservationDto.getInvoiceAddress().getCity())
                                .country(reservationDto.getInvoiceAddress().getCountry())
                                .nip(reservationDto.getInvoiceAddress().getNip())
                                .build()
                )
                .additionalServices(additionalservices)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .agreedToTerms(reservationDto.getAgreedToTerms())
                .agreedToParticipation(reservationDto.getAgreedToParticipation())
                .agreedToConditions(reservationDto.getAgreedToConditions())
                .build();

        reservationRepository.save(reservation);

        booth.setStatus(Booth.BoothStatus.RESERVED);
        booth.setCompany(reservationDto.getCompanyName());
        boothRepository.save(booth);

        return reservation;
    }

    // ===============================
    // CANCEL RESERVATION
    // ===============================
    @Transactional
    public void cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() ->
                        new RuntimeException("Nie znaleziono rezerwacji o ID: " + reservationId)
                );

        Booth booth = reservation.getBooth();

        // Przywrócenie stoiska
        booth.setStatus(Booth.BoothStatus.AVAILABLE);
        booth.setCompany(null);
        boothRepository.save(booth);

        // Usunięcie rezerwacji
        reservationRepository.delete(reservation);
    }

    // ===============================
    // QUERIES
    // ===============================
    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByContactEmail(email);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }
}
