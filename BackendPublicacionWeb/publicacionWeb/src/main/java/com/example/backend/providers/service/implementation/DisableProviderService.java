package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.usecase.DisableProviderUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DisableProviderService implements DisableProviderUsecase {

    private final ProviderRepository providerRepository;

    @Override
    public void disable(Long id) {
        log.info("Iniciando proceso para deshabilitar proveedor con id={}", id);

        Provider provider = providerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el proveedor con id={}", id);
                    return new RuntimeException("El proveedor con el id: " + id + " no existe");
                });

        log.debug("Proveedor encontrado: id={}, nombre={} {}",
                provider.getId(), provider.getName(), provider.getLastName());

        provider.setIsActive(false);
        log.info("Proveedor con id={} marcado como inactivo", id);

        providerRepository.save(provider);
        log.info("Proveedor con id={} guardado correctamente en la base de datos", id);
    }
}
