package com.example.backend.providers.service.implementation;

import com.example.backend.providers.controller.dto.ProviderResponseDTO;
import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.providers.service.Mapper.ProviderMapper;
import jakarta.persistence.ManyToOne;
import jdk.jfr.Enabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProvidersByNameOrCategoryServiceTest {

    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private ProviderMapper providerMapper;
    @InjectMocks
    private SearchProvidersByNameOrCategoryService service;


    @Test
    void execute_conNombreYCategoria_buscaPorNombreYCategoria() {
        String name = "Juan";
        String category = "Electricista";

        when(providerRepository.findByNameAndCategoryName(name, category))
                .thenReturn(List.of(new Provider()));
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        List<ProviderResponseDTO> result = service.execute(name, category);

        assertEquals(1, result.size());
        verify(providerRepository).findByNameAndCategoryName(name, category);
        verify(providerMapper).toDTO(any(Provider.class));
    }

    @Test
    void execute_soloNombre_buscaPorNombre() {
        when(providerRepository.findByName("Juan"))
                .thenReturn(List.of(new Provider()));
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        service.execute("Juan", null);

        verify(providerRepository).findByName("Juan");
    }

    @Test
    void execute_soloCategoria_buscaPorCategoria() {
        when(providerRepository.findByCategoryName("Plomero"))
                .thenReturn(List.of(new Provider()));
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        service.execute(null, "Plomero");

        verify(providerRepository).findByCategoryName("Plomero");
    }

    @Test
    void execute_sinParametros_buscaTodos() {
        when(providerRepository.findAll()).thenReturn(List.of(new Provider()));
        when(providerMapper.toDTO(any())).thenReturn(new ProviderResponseDTO());

        service.execute(null, null);

        verify(providerRepository).findAll();
    }


}