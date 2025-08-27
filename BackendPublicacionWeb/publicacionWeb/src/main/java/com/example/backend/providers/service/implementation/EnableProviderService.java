package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.usecase.EnableProviderUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnableProviderService implements EnableProviderUsecase {

    private final ProviderRepository providerRepository;

    @Override
    public void enable(Long id) {
        log.info("Iniciando proceso para habilitar proveedor con id={}", id);

        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el proveedor con id={}", id);
                    return new RuntimeException("El prestador con el id: " + id + " no existe");
                });

        log.debug("Proveedor encontrado: id={}, nombre={} {}, estadoActual={}",
                provider.getId(), provider.getName(), provider.getLastName(), provider.getIsActive());

        if (!Boolean.TRUE.equals(provider.getIsActive())) {
            provider.setIsActive(true);
            providerRepository.save(provider);
            log.info("Proveedor con id={} habilitado correctamente", id);
        } else {
            log.warn("El proveedor con id={} ya estaba habilitado, no se realizaron cambios", id);
        }
    }
}
