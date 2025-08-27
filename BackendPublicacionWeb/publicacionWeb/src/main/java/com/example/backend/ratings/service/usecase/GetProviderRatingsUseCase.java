package com.example.backend.ratings.service.usecase;

import com.example.backend.ratings.controller.dto.ProviderRatingDTO;

import java.util.List;

public interface GetProviderRatingsUseCase {

    public List<ProviderRatingDTO> execute();
}
