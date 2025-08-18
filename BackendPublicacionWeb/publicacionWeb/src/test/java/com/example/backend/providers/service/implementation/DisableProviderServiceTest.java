package com.example.backend.providers.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.users.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisableProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @InjectMocks
    private DisableProviderService disableProviderService;

    @Test
    void disable_proveedorExiste_loDesactivaYGuarda(){
        // Arrange
        Long providerId = 1L;
        Provider provider = new Provider();
        provider.setId(providerId);
        provider.setIsActive(true);

        // Mock the behavior of the repository to return the provider when findById is called
        when(providerRepository.findById(providerId)).thenReturn(java.util.Optional.of(provider));
        // Mock the save method to do nothing (or you can verify it was called later)
        when(providerRepository.save(provider)).thenReturn(provider);

        // Act
        disableProviderService.disable(providerId);

        // Assert
        assertFalse(provider.getIsActive(), "El proveedor debería estar desactivado");

        // Verify that the provider was found and saved with isActive set to false.
        verify( providerRepository).findById(providerId);
        verify(providerRepository).save(provider);
        // You can use Mockito's verify method to check interactions with the mock.
         verify(providerRepository).save(provider);


    }

    @Test
    void disable_proveedorNoExiste_lanzaExcepcion() {
        // Arrange
        Long providerId = 99L;
        when(providerRepository.findById(providerId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                disableProviderService.disable(providerId));

        assertEquals("El proveedor con el id: 99 no existe", ex.getMessage());
        verify(providerRepository).findById(providerId);
        verify(providerRepository, never()).save(any());
    }

}