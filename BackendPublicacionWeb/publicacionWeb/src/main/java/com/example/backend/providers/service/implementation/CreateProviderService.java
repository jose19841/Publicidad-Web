package com.example.backend.providers.service.implementation;

import com.example.backend.categories.domain.Category;
import com.example.backend.categories.infrastructure.CategoryRepository;
import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.CreateProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateProviderService implements CreateProviderUsecase {
    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public ProviderResponseDTO create(ProviderRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException(
                        "Categoría con id " + request.getCategoryId() + " no encontrada"
                ));

        Provider provider = providerMapper.toEntity(request);
        provider.setCategory(category); // 🔹 Asignamos la categoría al provider

        Provider saved = providerRepository.save(provider);
        return providerMapper.toDTO(saved);
    }
}
