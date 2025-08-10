package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.usecase.DisableProviderUsecase;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisableProviderService implements DisableProviderUsecase {
    private final ProviderRepository providerRepository;

    @Override
    public void disable(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El proveedor con el id: " + id + " no existe"));
        provider.setIsActive(false);
        providerRepository.save(provider);
    }
}

