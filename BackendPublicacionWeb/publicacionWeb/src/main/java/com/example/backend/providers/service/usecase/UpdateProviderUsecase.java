package com.example.backend.providers.service.usecase;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;

public interface UpdateProviderUsecase {
    ProviderResponseDTO update(Long id, ProviderUpdateRequestDTO request, MultipartFile image, String imageAction);
}
