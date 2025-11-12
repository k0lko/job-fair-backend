package com.JFBRA.service;

import com.JFBRA.dto.ServiceDto;
import com.JFBRA.model.AdditionalService;
import com.JFBRA.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public List<ServiceDto> getAllServices() {
        List<AdditionalService> services = serviceRepository.findByIsActive(true);
        return services.stream()
                .map(this::convertToDto)
                .toList();
    }

    public ServiceDto getServiceByCode(String serviceCode) {
        AdditionalService service = serviceRepository.findByServiceCode(serviceCode);
        return service != null ? convertToDto(service) : null;
    }

    private ServiceDto convertToDto(AdditionalService service) {
        return ServiceDto.builder()
                .serviceCode(service.getServiceCode())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .vat(service.getVat())
                .build();
    }
}
