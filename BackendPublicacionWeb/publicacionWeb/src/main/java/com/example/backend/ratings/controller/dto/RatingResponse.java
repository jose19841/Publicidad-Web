package com.example.backend.ratings.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
@Data
public class RatingResponse {
    private Long ratingId;
    private Long providerId;
    private Integer score;
    private double providerAverage;
    private long providerRatingCount;
}