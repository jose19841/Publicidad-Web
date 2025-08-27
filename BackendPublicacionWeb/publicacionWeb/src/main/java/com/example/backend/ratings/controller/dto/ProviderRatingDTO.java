package com.example.backend.ratings.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa las estadísticas de calificación de un proveedor,
 * incluyendo sus datos básicos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderRatingDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String category;
    private Long ratingsCount;
    private Double averageScore;
}
