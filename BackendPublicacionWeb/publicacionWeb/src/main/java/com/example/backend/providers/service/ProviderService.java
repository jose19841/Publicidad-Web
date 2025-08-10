package com.example.backend.providers.service;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.service.usecase.CreateProviderUsecase;
import com.example.backend.providers.service.usecase.DisableProviderUsecase;
import com.example.backend.providers.service.usecase.GetAllProviderUsecase;
import com.example.backend.providers.service.usecase.UpdateProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final CreateProviderUsecase createProviderUsecase;
    private final GetAllProviderUsecase getAllProviderUsecase;
    private final UpdateProviderUsecase updateProviderUsecase;
    private final DisableProviderUsecase disableProviderUsecase;

    public ProviderResponseDTO create(ProviderRequestDTO request){
        return createProviderUsecase.create(request);
    }

    public List<ProviderResponseDTO> getAll(){
        return getAllProviderUsecase.getAll();
    }

    public ProviderResponseDTO update(long id, ProviderRequestDTO request){
        return updateProviderUsecase.update(id, request);
    }

    public void disable(Long id){
        disableProviderUsecase.disable(id);
    }
}
