package com.example.backend.providers.service.usecase;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;

import java.util.List;

public interface SearchProvidersByNameOrCategoryUsecase {
    List<ProviderResponseDTO> execute(String name, String category);
}
