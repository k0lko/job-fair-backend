package com.JFBRA.service;

import com.JFBRA.dto.ReservationDto;
import com.JFBRA.model.AdditionalService;
import com.JFBRA.model.Booth;
import com.JFBRA.model.Reservation;
import com.JFBRA.repository.BoothRepository;
import com.JFBRA.repository.ReservationRepository;
import com.JFBRA.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ServiceRepository serviceRepository;
    private final BoothRepository boothRepository;

    @Transactional
    public Reservation createReservation(ReservationDto reservationDto) {
        //Sprawdzenie, czy stanowisko istnieje
        Booth booth = boothRepository.findById(reservationDto.getBoothId())
                .orElseThrow(() -> new RuntimeException("Nie znaleziono stoiska o ID: " + reservationDto.getBoothId()));

        if (!Booth.BoothStatus.AVAILABLE.equals(booth.getStatus())) {
            throw new RuntimeException("To stoisko nie jest dostępne do rezerwacji");
        }

        //Sprawdzenie, czy dany email nie ma już rezerwacji
        if (reservationRepository.existsByContactEmail(reservationDto.getContactEmail())) {
            throw new RuntimeException("Ten adres email ma już istniejącą rezerwację");
        }

        //  Pobranie usług po ID (obsługa błędnego formatu)
        List<Long> serviceIds;
        try {
            serviceIds = reservationDto.getServices().stream()
                    .map(Long::valueOf)
                    .toList();
        } catch (NumberFormatException e) {
            throw new RuntimeException("Niepoprawny format ID usługi: " + e.getMessage());
        }

        List<AdditionalService> additionalServices = serviceRepository.findAllById(serviceIds);

        //Obliczenie łącznej ceny
        int servicesTotal = additionalServices.stream()
                .mapToInt(AdditionalService::getPrice)
                .sum();

        int totalAmount = booth.getPrice() + servicesTotal;

        //Tworzenie rezerwacji z nową strukturą adresu fakturowego
        Reservation reservation = Reservation.builder()
                .booth(booth)
                .companyName(reservationDto.getCompanyName())
                .industry(reservationDto.getIndustry())
                .website(reservationDto.getWebsite())
                .contactName(reservationDto.getContactName())
                .contactEmail(reservationDto.getContactEmail())
                .contactPhone(reservationDto.getContactPhone())
                .invoiceAddress(Reservation.InvoiceAddress.builder()
                        .companyName(reservationDto.getInvoiceAddress().getCompanyName())
                        .street(reservationDto.getInvoiceAddress().getStreet())
                        .postalCode(reservationDto.getInvoiceAddress().getPostalCode())
                        .city(reservationDto.getInvoiceAddress().getCity())
                        .country(reservationDto.getInvoiceAddress().getCountry())
                        .nip(reservationDto.getInvoiceAddress().getNip())
                        .build())
                .additionalServices(additionalServices)
                .totalAmount(totalAmount)
                .createdAt(LocalDateTime.now())
                .agreedToTerms(reservationDto.getAgreedToTerms())
                .agreedToParticipation(reservationDto.getAgreedToParticipation())
                .agreedToConditions(reservationDto.getAgreedToConditions())
                .build();

        //Zapis rezerwacji
        reservation = reservationRepository.save(reservation);

        //Aktualizacja statusu stoiska
        booth.setStatus(Booth.BoothStatus.RESERVED);
        booth.setCompany(reservationDto.getCompanyName());
        boothRepository.save(booth);


        return reservation;
    }

    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByContactEmail(email);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }
}