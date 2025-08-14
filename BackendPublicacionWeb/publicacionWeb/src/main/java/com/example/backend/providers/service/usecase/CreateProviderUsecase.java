package com.example.backend.providers.service.usecase;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import org.springframework.web.multipart.MultipartFile;

public interface CreateProviderUsecase {
    ProviderResponseDTO create(ProviderRequestDTO request, MultipartFile image);
}
