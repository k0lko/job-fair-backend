package com.JFBRA.service;

import com.JFBRA.dto.BoothDto;
import com.JFBRA.model.Booth;
import com.JFBRA.repository.BoothRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoothService {

    private final BoothRepository boothRepository;

    public List<BoothDto> getAllBooths() {
        List<Booth> booths = boothRepository.findAll();
        return BoothDto.fromEntities(booths);
    }

    public List<BoothDto> getAvailableBooths() {
        List<Booth> booths = boothRepository.findByStatus(Booth.BoothStatus.AVAILABLE);
        return BoothDto.fromEntities(booths);
    }

    public BoothDto getBoothByNumber(String boothNumber) {
        Booth booth = boothRepository.findByBoothNumber(boothNumber);
        return BoothDto.fromEntity(booth);
    }

    public BoothDto getBoothById(Long id) {
        Booth booth = boothRepository.findById(id).orElse(null);
        return BoothDto.fromEntity(booth);
    }

    @Transactional
    public void updateBoothStatus(Long boothId, Booth.BoothStatus status, String companyName) {
        Booth booth = boothRepository.findById(boothId)
                .orElseThrow(() -> new RuntimeException("Booth not found with id: " + boothId));

        booth.setStatus(status);
        if (companyName != null) {
            booth.setCompany(companyName);
        }

        boothRepository.save(booth);
    }
}