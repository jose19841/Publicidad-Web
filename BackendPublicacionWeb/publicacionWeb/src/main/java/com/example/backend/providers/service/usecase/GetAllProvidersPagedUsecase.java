package com.example.backend.providers.service.usecase;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import org.springframework.data.domain.Page;

public interface GetAllProvidersPagedUsecase {

    Page<ProviderResponseDTO> execute(int page, int size);
}
