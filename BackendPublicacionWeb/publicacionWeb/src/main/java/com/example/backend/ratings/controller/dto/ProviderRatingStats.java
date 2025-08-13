package com.example.backend.ratings.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class ProviderRatingStats {
    private final double average;
    private final long count;
}