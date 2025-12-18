package com.JFBRA.service;

import com.JFBRA.dto.ServiceDto;
import com.JFBRA.model.AdditionalService;
import com.JFBRA.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public List<ServiceDto> getAllServices() {
        List<AdditionalService> additionalServices = serviceRepository.findByIsActive(true);
        return additionalServices.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ServiceDto getServiceByCode(String serviceCode) {
        AdditionalService additionalService = serviceRepository.findByServiceCode(serviceCode);
        return additionalService != null ? convertToDto(additionalService) : null;
    }

    private ServiceDto convertToDto(AdditionalService additionalService) {
        return ServiceDto.builder()
                .serviceCode(additionalService.getServiceCode())
                .name(additionalService.getName())
                .description(additionalService.getDescription())
                .price(additionalService.getPrice())
                .vat(additionalService.getVat())
                .build();
    }
}
