package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.usecase.EnableProviderUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnableProviderService implements EnableProviderUsecase {
    private final ProviderRepository providerRepository;

    @Override
    public void enable(Long id) {
        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El prestador con el id: " + id + " No existe"));
        if(!Boolean.TRUE.equals(provider.getIsActive())){
            provider.setIsActive(true);
            providerRepository.save(provider);
        }
    }
}
