package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import com.example.backend.providers.service.usecase.SearchProvidersByNameOrCategoryUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchProvidersByNameOrCategoryService implements SearchProvidersByNameOrCategoryUsecase {
    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    @Override
    public List<ProviderResponseDTO> execute(String name, String category) {
        log.info("Iniciando búsqueda de proveedores con parámetros -> nombre: {}, categoría: {}", name, category);

        List<Provider> providers;
        if (name != null && category != null) {
            log.debug("Buscando proveedores por nombre y categoría");
            providers = providerRepository.findByNameAndCategoryName(name, category);
        } else if (name != null) {
            log.debug("Buscando proveedores por nombre");
            providers = providerRepository.findByName(name);
        } else if (category != null) {
            log.debug("Buscando proveedores por categoría");
            providers = providerRepository.findByCategoryName(category);
        } else {
            log.debug("No se pasaron filtros, obteniendo todos los proveedores");
            providers = providerRepository.findAll();
        }

        List<ProviderResponseDTO> result = providers.stream()
                .map(providerMapper::toDTO)
                .toList();

        log.info("Se encontraron {} proveedores con los filtros aplicados", result.size());
        return result;
    }
}
