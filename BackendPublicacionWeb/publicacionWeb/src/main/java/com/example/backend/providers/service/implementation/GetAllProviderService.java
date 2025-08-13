package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.GetAllProviderUsecase;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllProviderService implements GetAllProviderUsecase {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final RatingRepository ratingRepository;

    @Override
    public List<ProviderResponseDTO> getAll() {
        return providerRepository.findAll()
                .stream()
                .map(provider -> {
                    ProviderResponseDTO dto = providerMapper.toDTO(provider);

                    ProviderRatingStats stats =
                            ratingRepository.getAvgAndCountByProviderId(provider.getId());

                    dto.setAverageRating(stats.getAverage());
                    dto.setTotalRatings(stats.getCount());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
