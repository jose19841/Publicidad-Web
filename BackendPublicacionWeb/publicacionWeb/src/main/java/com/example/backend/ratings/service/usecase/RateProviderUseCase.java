package com.example.backend.ratings.service.usecase;

import com.example.backend.ratings.controller.dto.RatingResponse;

public interface RateProviderUseCase {
    RatingResponse execute(Long providerId, Long currentUserId, int score);
}