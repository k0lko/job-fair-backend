package com.JFBRA.controller;

import com.JFBRA.dto.BoothDto;
import com.JFBRA.dto.*;
import com.JFBRA.service.BoothService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booths")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"})
public class BoothController {

    private final BoothService boothService;

    @GetMapping
    public ResponseEntity<List<BoothDto>> getAllBooths() {
        List<BoothDto> booths = boothService.getAllBooths();
        return ResponseEntity.ok(booths);
    }

    @GetMapping("/available")
    public ResponseEntity<List<BoothDto>> getAvailableBooths() {
        List<BoothDto> booths = boothService.getAvailableBooths();
        return ResponseEntity.ok(booths);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoothDto> getBoothById(@PathVariable Long id) {
        BoothDto booth = boothService.getBoothById(id);
        if (booth != null) {
            return ResponseEntity.ok(booth);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/number/{boothNumber}")
    public ResponseEntity<BoothDto> getBoothByNumber(@PathVariable String boothNumber) {
        BoothDto booth = boothService.getBoothByNumber(boothNumber);
        if (booth != null) {
            return ResponseEntity.ok(booth);
        }
        return ResponseEntity.notFound().build();
    }
}