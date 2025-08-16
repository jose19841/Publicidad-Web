package com.example.backend.providers.service;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.controller.dto.ProviderUpdateRequestDTO;
import com.example.backend.providers.service.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final CreateProviderUsecase createProviderUsecase;
    private final GetAllProviderUsecase getAllProviderUsecase;
    private final UpdateProviderUsecase updateProviderUsecase;
    private final DisableProviderUsecase disableProviderUsecase;
    private final SearchProviderUsecase searchProviderService;
    private final EnableProviderUsecase enableProviderUsecase;

    public ProviderResponseDTO create(ProviderRequestDTO request, MultipartFile image) {
        return createProviderUsecase.create(request, image);
    }

    public List<ProviderResponseDTO> getAll() {
        return getAllProviderUsecase.getAll();
    }

    public ProviderResponseDTO update(long id, ProviderUpdateRequestDTO request, MultipartFile image, String imageAction) {
        return updateProviderUsecase.update(id, request, image, imageAction);
    }

    public void disable(Long id) {
        disableProviderUsecase.disable(id);
    }
    public void enable(Long id){enableProviderUsecase.enable(id);}

    public ProviderResponseDTO search(Long providerId) {
        return searchProviderService.execute(providerId);
    }
}


