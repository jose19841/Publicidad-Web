package com.example.backend.users.service.implement;

import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.EmailService;
import com.example.backend.users.service.usecase.SendResetTokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenResetTokenUseCaseTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ResetTokenRepository resetTokenRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private SenResetTokenUseCase sendResetTokenUseCase;


    @Test
    void execute_usuarioValidoSinTokenPrevio_enviaCorreoYGuardaToken() {
        // Arrange
        String email = "joni.illes@gmail.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(resetTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        // Act
        sendResetTokenUseCase.execute(email);
        // Assert
        // Capturar el token guardado
        ArgumentCaptor<ResetToken> tokenCaptor = ArgumentCaptor.forClass(ResetToken.class);
        verify(resetTokenRepository).save(tokenCaptor.capture());

        ResetToken token = tokenCaptor.getValue();
        assertThat(token).isNotNull();
        assertThat(token.getUser()).isEqualTo(user);
        assertThat(token.getExpiryDate()).isAfter(LocalDateTime.now());

        // Verificar envío de correo
        verify(emailService).sendHtml(
                eq(email),
                eq("Recuperación de contraseña"),
                contains("reset-password?token=")
        );

        verify(resetTokenRepository, never()).delete(any());

    }


    @Test
    void execute_usuarioConTokenVencido_eliminaTokenPrevioYGeneraUnoNuevo() {
        // Arrange
        String email = "joni.illes@gmail.com";
        User user = new User();
        user.setEmail(email);
        ResetToken expiredToken = new ResetToken("expired-token", user, LocalDateTime.now().minusMinutes(10));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(resetTokenRepository.findByUser(user)).thenReturn(Optional.of(expiredToken));

        // Act
        sendResetTokenUseCase.execute(email);

        // Assert
        // 1. Se eliminó el token vencido
        verify(resetTokenRepository).delete(expiredToken);

        // 2. Se guardó un nuevo token
        ArgumentCaptor<ResetToken> tokenCaptor = ArgumentCaptor.forClass(ResetToken.class);
        verify(resetTokenRepository).save(tokenCaptor.capture());
        ResetToken newToken = tokenCaptor.getValue();

        assertThat(newToken).isNotNull();
        assertThat(newToken.getUser()).isEqualTo(user);
        assertThat(newToken.getExpiryDate()).isAfter(LocalDateTime.now().plusMinutes(29));

        // 3. Se envió el correo con el link correcto
        verify(emailService).sendHtml(
                eq(email),
                eq("Recuperación de contraseña"),
                contains("reset-password?token=")
        );
    }


    @Test
    void execute_usuarioNoEncontrado_lanzaRuntimeException() {
        // Arrange
        String email = "joni.illes@gmail.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            sendResetTokenUseCase.execute(email);
        });
        assertThat(exception.getMessage()).isEqualTo("Usuario no encontrado");
        verify(resetTokenRepository, never()).delete(any());
        verify(resetTokenRepository, never()).save(any());
        verify(emailService, never()).sendHtml(anyString(), anyString(), anyString());

    }


    @Test
    void execute_usuarioConTokenValido_lanzaIllegalStateException() {
        // Arrange
        String email = "joni.illes@hotmail.com";
        User user = new User();
        user.setEmail(email);
        ResetToken validToken = new ResetToken("valid-token", user, LocalDateTime.now().plusMinutes(10));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(resetTokenRepository.findByUser(user)).thenReturn(Optional.of(validToken));
        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            sendResetTokenUseCase.execute(email);
        });
        assertThat(exception.getMessage()).isEqualTo("Ya se ha enviado el correo de recuperación correctamente. Verifique su casilla de correo o en la sección de Spam.");
        verify(resetTokenRepository, never()).delete(any());
        verify(resetTokenRepository, never()).save(any());
        verify(emailService, never()).sendHtml(anyString(), anyString(), anyString());

    }


}