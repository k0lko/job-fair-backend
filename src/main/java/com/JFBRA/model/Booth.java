package com.JFBRA.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
@Entity
@Table(name = "booths")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booth_number", nullable = false, unique = true)
    private String boothNumber;

    @Column(nullable = false)
    private Integer x;

    @Column(nullable = false)
    private Integer y;

    @Column(nullable = false)
    private Integer width;

    @Column(nullable = false)
    private Integer height;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoothSize size;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoothStatus status;

    @Column
    private String company;

    /** Rozmiary stanowisk **/
    public enum BoothSize {
        SIZE_1X1("1x1"),
        SIZE_2X1("2x1");

        private final String displayName;

        BoothSize(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
    /** Statusy stanowisk **/
    public enum BoothStatus {
        AVAILABLE,
        RESERVED,
        OCCUPIED
    }
}
