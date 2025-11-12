package com.JFBRA.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;

    @NotNull(message = "ID stoiska jest wymagane")
    private Long boothId;

    @NotBlank(message = "Nazwa firmy jest wymagana")
    @Size(max = 255, message = "Nazwa firmy nie może przekroczyć 255 znaków")
    private String companyName;

    @Size(max = 255, message = "Branża nie może przekroczyć 255 znaków")
    private String industry;

    @Size(max = 500, message = "Strona WWW nie może przekroczyć 500 znaków")
    private String website;

    @NotBlank(message = "Imię i nazwisko są wymagane")
    @Size(max = 255, message = "Imię i nazwisko nie może przekroczyć 255 znaków")
    private String contactName;

    @NotBlank(message = "Email jest wymagany")
    @Email(message = "Wprowadź poprawny adres email")
    @Size(max = 255, message = "Email nie może przekroczyć 255 znaków")
    private String contactEmail;

    @NotBlank(message = "Telefon jest wymagany")
    @Pattern(regexp = "^[+]?[\\d\\s\\-\\(\\)]+$", message = "Wprowadź poprawny numer telefonu")
    @Size(max = 50, message = "Telefon nie może przekroczyć 50 znaków")
    private String contactPhone;

    @Valid
    @NotNull(message = "Dane do faktury są wymagane")
    private InvoiceAddressDto invoiceAddress;

    private List<String> services = new ArrayList<>();

    @NotNull(message = "Zgoda na przetwarzanie danych jest wymagana")
    private Boolean agreedToTerms;

    @NotNull(message = "Zgoda na udział jest wymagana")
    private Boolean agreedToParticipation;

    @NotNull(message = "Zgoda na warunki jest wymagana")
    private Boolean agreedToConditions;


    // 🔹 Nowa, rozszerzona wersja InvoiceAddressDto
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceAddressDto {
        @NotBlank(message = "Nazwa firmy na fakturze jest wymagana")
        @Size(max = 255, message = "Nazwa firmy na fakturze nie może przekroczyć 255 znaków")
        private String companyName;

        @NotBlank(message = "Ulica jest wymagana")
        @Size(max = 255, message = "Ulica nie może przekroczyć 255 znaków")
        private String street;

        @NotBlank(message = "Kod pocztowy jest wymagany")
        @Pattern(regexp = "^[0-9]{2}-[0-9]{3}$", message = "Wprowadź poprawny kod pocztowy (format 00-000)")
        private String postalCode;

        @NotBlank(message = "Miasto jest wymagane")
        @Size(max = 255, message = "Miasto nie może przekroczyć 255 znaków")
        private String city;

        @Size(max = 255, message = "Kraj nie może przekroczyć 255 znaków")
        private String country;

        @NotBlank(message = "NIP jest wymagany")
        @Pattern(regexp = "^(\\d{10}|\\d{3}-\\d{3}-\\d{2}-\\d{2})$", message = "Wprowadź poprawny NIP")
        private String nip;
    }
}
