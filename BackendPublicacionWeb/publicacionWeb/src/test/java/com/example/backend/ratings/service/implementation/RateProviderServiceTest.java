package com.example.backend.ratings.service.implementation;

import com.example.backend.providers.domain.Provider;
import com.example.backend.providers.infrastructure.ProviderRepository;
import com.example.backend.ratings.controller.dto.ProviderRatingStats;
import com.example.backend.ratings.controller.dto.RatingResponse;
import com.example.backend.ratings.domain.Rating;
import com.example.backend.ratings.infrastructure.RatingRepository;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RateProviderServiceTest {

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private ProviderRepository providerRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private RateProviderService rateProviderService;

    @Test
    void execute_scoreInvalido_lanzaIllegalArgumentException() {
        // Arrange
        Long providerId = 1L;
        Long currentUserId = 1L;
        int score = 6; // Score fuera del rango válido (1-5)
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rateProviderService.execute(providerId, currentUserId, score);
        });
        assertEquals("La calificación debe ser entre 1 y 5.", exception.getMessage());
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void execute_usuarioYaCalifico_lanzaIllegalStateException() {
        // Arrange
        Long providerId = 1L;
        Long currentUserId = 1L;
        int score = 5; // Score válido
        // Simular que el usuario ya calificó al proveedor
        when(ratingRepository.existsByUserIdAndProviderId(currentUserId, providerId)).thenReturn(true);

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            rateProviderService.execute(providerId, currentUserId, score);
        });
        assertEquals("Ya calificaste a este prestador.", exception.getMessage());
        verify(ratingRepository, never()).save(any());


    }

    @Test
    void execute_usuarioNoEncontrado_lanzaIllegalArgumentException() {
        // Arrange
        Long providerId = 1L;
        Long currentUserId = 1L;
        int score = 5; // Score válido
        // Simular que el usuario no existe
        when(userRepository.findById(currentUserId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rateProviderService.execute(providerId, currentUserId, score);
        });
        assertEquals("Usuario no encontrado.", exception.getMessage());
        verify(ratingRepository, never()).save(any());

    }

    @Test
    void execute_proveedorNoEncontrado_lanzaIllegalArgumentException() {
        // Arrange
        Long providerId = 1L;
        Long currentUserId = 1L;
        int score = 5; // Score válido
        // Simular que el proveedor no existe
        when(userRepository.findById(currentUserId)).thenReturn(java.util.Optional.of(new User())); // Simular usuario existente
        when(providerRepository.findById(providerId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rateProviderService.execute(providerId, currentUserId, score);
        });
        assertEquals("Prestador no encontrado.", exception.getMessage());
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void execute_calificacionExitosa_retornaRatingResponse() {
        // Arrange
        Long providerId = 1L;
        Long currentUserId = 1L;
        int score = 5; // Score válido
        User user = new User();
        user.setId(currentUserId);
        when(userRepository.findById(currentUserId)).thenReturn(java.util.Optional.of(user));
        Provider provider = new Provider();
        provider.setId(providerId);
        when(providerRepository.findById(providerId)).thenReturn(java.util.Optional.of(provider));
        when(ratingRepository.existsByUserIdAndProviderId(currentUserId, providerId)).thenReturn(false);
        when(ratingRepository.save(any())).thenAnswer(invocation -> {
            Rating rating = invocation.getArgument(0);
            rating.setId(1L); // Simular que se asigna un ID al guardar
            return rating;
        });
        when(ratingRepository.getStatsByProviderId(providerId)).thenReturn(new ProviderRatingStats(4.5, 10)); // Simular estadísticas del proveedor
        // Act
        RatingResponse response = rateProviderService.execute(providerId, currentUserId, score);
        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getRatingId());
        assertEquals(providerId, response.getProviderId());
        assertEquals(score, response.getScore());
        assertEquals(4.5, response.getProviderAverage());
        assertEquals(10L, response.getProviderRatingCount());

        ArgumentCaptor<Rating> captor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(captor.capture());

        Rating capturado = captor.getValue();
        assertEquals(user, capturado.getUser());
        assertEquals(provider, capturado.getProvider());
        assertEquals(score, capturado.getScore());

        verify(ratingRepository).existsByUserIdAndProviderId(currentUserId, providerId);
        verify(userRepository).findById(currentUserId);
        verify(providerRepository).findById(providerId);
        verify(ratingRepository).getStatsByProviderId(providerId);
        verifyNoMoreInteractions(ratingRepository, userRepository, providerRepository);
    }
}