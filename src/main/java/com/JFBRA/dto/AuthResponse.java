package com.JFBRA.dto;

public record AuthResponse(String token, long expiresAtMillis) {}
