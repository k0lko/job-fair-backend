package com.JFBRA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {

    private String serviceCode;
    private String name;
    private String description;
    private Integer price;
    private Integer vat;
}