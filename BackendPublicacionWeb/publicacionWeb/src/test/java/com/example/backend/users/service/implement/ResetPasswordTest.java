package com.example.backend.users.service.implement;

import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.ResetTokenRepository;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetPasswordTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private ResetTokenRepository resetTokenRepository;
    @InjectMocks
    private ResetPassword resetPassword;

    @Test
    void execute_tokenInexistente_lanzaRuntimeException() {
        // Arrange
        String token = "invalid-token";
        String newPassword = "newPassword123";
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resetPassword.execute(token, newPassword);
        });

        assertEquals("Token inválido", exception.getMessage());
    }


    @Test
    void execute_conTokenExpirado_lanzaExcepcionConMensajeAdecuado()
    {
        // Arrange
        String token = "expired-token";
        String newPassword = "newPassword123";
        ResetToken expiredToken = new ResetToken();
        expiredToken.setExpiryDate(LocalDateTime.now().minusDays(1));
        User dummyUser = new User();
        dummyUser.setEmail("test@test.com");
        expiredToken.setUser(dummyUser);
        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(expiredToken));
        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resetPassword.execute(token, newPassword);
        });
        assertEquals("El enlace de recuperación ha expirado.", exception.getMessage());


    }

    @Test
    void execute_tokenValido_actualizaPasswordYEliminaToken() {
        // Arrange
        String token = "valid-token";
        String newPassword = "newPassword123";
        User user = new User();
        ResetToken validToken = new ResetToken();
        validToken.setExpiryDate(LocalDateTime.now().plusDays(1));
        validToken.setUser(user);

        when(resetTokenRepository.findByToken(token)).thenReturn(Optional.of(validToken));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // Act
        resetPassword.execute(token, newPassword);

        // Assert
        assertEquals("encodedPassword", user.getPassword(),
                "La contraseña debería haberse actualizado correctamente");

        // Verifica que el usuario se haya guardado
        verify(userRepository).save(user);

        // Verifica que el token se haya eliminado
        verify(resetTokenRepository).delete(validToken);

        // Verifica que se haya usado el passwordEncoder
        verify(passwordEncoder).encode(newPassword);
    }


}