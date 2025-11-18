package com.JFBRA.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    // Authentication fields
    @Column
    private String password; // nullable for existing users until they set password

    /**
     * store roles as comma separated values, e.g. "USER" or "ADMIN,USER"
     */
    @Column
    private String roles;

    @Column(nullable = false)
    private Boolean isLoggedIn;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastLoginAt;

    // Helpers

    public Set<String> getRolesAsSet() {
        if (roles == null || roles.isBlank()) return Set.of();
        return Arrays.stream(roles.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public void setRolesFromSet(Set<String> roleSet) {
        this.roles = String.join(",", roleSet);
    }
}
