package com.example.backend.users.service.implement;

import com.example.backend.config.security.JwtService;
import com.example.backend.shared.exceptions.InvalidCredentialsException;
import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    @Test
   void intentoDeLoginConUsuarioNoExistente() {

            // Arrange
            AuthenticationRequest request = new AuthenticationRequest();
            request.setEmail("noexiste@mail.com");
            request.setPassword("1234");

            when(userRepository.findByEmail("noexiste@mail.com"))
                    .thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> authenticationService.login(request))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessageContaining("Usuario no encontrado");

            // Verificar que no se llamaron los otros métodos
            verify(authenticationManager, never()).authenticate(any());
            verify(userRepository, never()).save(any());
            verify(jwtService, never()).generateToken(anyMap(), any());
        }




    @Test
    void intentoDeLoginConUsuarioDeshabilitado() {

        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");
        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(false); // Usuario deshabilitado
        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        // Act + Assert
        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(com.example.backend.shared.exceptions.DisabledUserException.class)
                .hasMessageContaining("El usuario está deshabilitado");
        // Verificar que no se llamaron los otros métodos
        verify(authenticationManager, never()).authenticate(any());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyMap(), any());

    }

    @Test
    void intentoDeLoginConCredencialesIncorrectas() {
        // Aquí deberías implementar el test para intentar hacer login con credenciales incorrectas
        // y verificar que se lanza la excepción InvalidCredentialsException.
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("wrongpassword");
        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true); // Usuario habilitado
        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));
        // Act + Assert
        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Contraseña incorrecta");
        // Verificar que no se llamaron los otros métodos
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyMap(), any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginExitoso() {
        // Aquí deberías implementar el test para un login exitoso
        // y verificar que se devuelve un AuthenticationResponse con un token JWT válido.

        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");
        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true); // Usuario habilitado
        user.setLastLoginAt(null); // Último login no establecido
        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("mocked-jwt-token");
        // Simular autenticación exitosa
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));
        // Act
        var response = authenticationService.login(request);
        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        // Verificar que se actualizó el último login
        verify(userRepository).save(user);
        assertNotNull(user.getLastLoginAt());
        assertTrue(user.getLastLoginAt().isBefore(LocalDateTime.now()));
    }

    @Test
    void errorGenerandoTokenJWT() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");

        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true);

        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenThrow(new RuntimeException("Error generando token JWT"));
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));

        // Act + Assert
        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error generando token JWT");

        // ✅ Se actualizó el último login ANTES del fallo
        verify(userRepository).save(user);
        assertNotNull(user.getLastLoginAt());

        // ✅ Sí se intentó generar token y falló
        verify(jwtService).generateToken(anyMap(), any(User.class));

        // ✅ Sí se autenticó
        verify(authenticationManager).authenticate(any());
    }


    @Test
    void persistenciaDelUltimoLogin() {
        // Aquí deberías implementar el test para verificar que se actualiza correctamente
        // el campo lastLoginAt del usuario después de un login exitoso.
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");
        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true); // Usuario habilitado
        user.setLastLoginAt(null); // Último login no establecido
        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("mocked-jwt-token");
        // Simular autenticación exitosa
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));
        // Act
        var response = authenticationService.login(request);
        // Assert
        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        // Verificar que se actualizó el último login
        verify(userRepository).save(user);
        assertNotNull(user.getLastLoginAt());
        assertTrue(user.getLastLoginAt().isBefore(LocalDateTime.now()));

    }


}