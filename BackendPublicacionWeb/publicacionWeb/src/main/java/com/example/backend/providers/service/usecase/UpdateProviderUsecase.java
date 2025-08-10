package com.example.backend.providers.service.usecase;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;

public interface UpdateProviderUsecase {
    ProviderResponseDTO update(Long id, ProviderRequestDTO request);
}
