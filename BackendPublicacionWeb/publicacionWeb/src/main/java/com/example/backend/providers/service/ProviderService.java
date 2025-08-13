package com.example.backend.providers.service;

import com.example.backend.providers.controller.dto.ProviderRequestDTO;
import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.service.usecase.*;
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
    private final SearchProviderUsecase searchProviderService;

    public ProviderResponseDTO create(ProviderRequestDTO request){
        return createProviderUsecase.create(request);
    }

    public List<ProviderResponseDTO> getAll(){
        return getAllProviderUsecase.getAll();
    }

    public ProviderResponseDTO update(long id, ProviderRequestDTO request){return updateProviderUsecase.update(id, request);}

    public void disable(Long id){disableProviderUsecase.disable(id);}

    public ProviderResponseDTO search(Long providerId) {return searchProviderService.execute(providerId);}
}
