package com.example.backend.admin.service;

import com.example.backend.admin.controller.dto.DashboardMetricsDTO;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.users.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProviderRepository providerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;

    public DashboardMetricsDTO getMetrics() {
        long totalProviders = providerRepository.count();
        long totalCategories = categoryRepository.count();
        long totalUsers = userRepository.count();
        double averageRating = ratingRepository.getAverageScore();

        var providersByCategory = providerRepository.countProvidersByCategory();
        var ratingsDistribution = ratingRepository.getRatingsDistribution();

        return new DashboardMetricsDTO(
                totalProviders,
                totalCategories,
                totalUsers,
                averageRating,
                providersByCategory,
                ratingsDistribution
        );
    }
}
