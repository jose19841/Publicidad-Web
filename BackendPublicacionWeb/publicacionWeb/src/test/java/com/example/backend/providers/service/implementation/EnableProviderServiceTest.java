package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EnableProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private EnableProviderService enableProviderService;

    @Test
    void disable_proveedorExiste_loActivaYGuarda(){
        // Arrange
        Long providerId = 1L;
        Provider provider = new Provider();
        provider.setId(providerId);
        provider.setIsActive(false);

        when(providerRepository.findById(providerId)).thenReturn(Optional.of(provider));

        // Act
        enableProviderService.enable(providerId);

        // Assert
        assertTrue(provider.getIsActive());
        verify(providerRepository).save(provider);
    }

    @Test
    void disable_proveedorNoExiste_lanzaExcepcion() {
        // Arrange
        Long providerId = 1L;
        when(providerRepository.findById(providerId)).thenReturn(Optional.empty());
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            enableProviderService.enable(providerId);
        });
        assertEquals("El prestador con el id: " + providerId + " no existe", exception.getMessage());
        verify( providerRepository).findById(providerId);

    }


}