package com.example.backend.admin.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DashboardMetricsDTO {
    private long totalProviders;
    private long totalCategories;
    private long totalUsers;
    private double averageRating;

    private List<CategoryCountDTO> providersByCategory;
    private List<RatingCountDTO> ratingsDistribution;
}