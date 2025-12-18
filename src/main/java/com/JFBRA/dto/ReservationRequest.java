package com.JFBRA.dto;

import lombok.Data;

@Data
public class ReservationRequest {

    private Long boothId;

    private String companyName;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    private Integer totalAmount;
}
