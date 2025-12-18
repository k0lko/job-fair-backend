package com.JFBRA.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@Entity
@Table(name = "reservations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booth_id", nullable = false)
    private Booth booth;

    @Column(nullable = false)
    private String companyName;

    private String industry;

    private String website;

    @Column(nullable = false)
    private String contactName;

    @Column(nullable = false, unique = true)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "companyName", column = @Column(name = "invoice_company_name")),
            @AttributeOverride(name = "street", column = @Column(name = "invoice_street")),
            @AttributeOverride(name = "postalCode", column = @Column(name = "invoice_postal_code")),
            @AttributeOverride(name = "city", column = @Column(name = "invoice_city")),
            @AttributeOverride(name = "country", column = @Column(name = "invoice_country")),
            @AttributeOverride(name = "nip", column = @Column(name = "invoice_nip"))
    })
    private InvoiceAddress invoiceAddress;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "reservation_services",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<AdditionalService> additionalServices;

    @Column(nullable = false)
    private Integer totalAmount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean agreedToTerms;

    @Column(nullable = false)
    private Boolean agreedToParticipation;

    @Column(nullable = false)
    private Boolean agreedToConditions;

    @Embeddable
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InvoiceAddress {

        @Column(nullable = false)
        private String companyName;

        @Column(nullable = false)
        private String street;

        @Column(nullable = false)
        private String postalCode;

        @Column(nullable = false)
        private String city;

        @Column
        private String country;

        @Column(nullable = false)
        private String nip;
    }
}
