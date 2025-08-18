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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.within;


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
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("noexiste@mail.com");
        request.setPassword("1234");

        when(userRepository.findByEmail("noexiste@mail.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(authenticationManager, never()).authenticate(any());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyMap(), any());
    }

    @Test
    void intentoDeLoginConUsuarioDeshabilitado() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");

        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(false);

        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(com.example.backend.shared.exceptions.DisabledUserException.class)
                .hasMessageContaining("El usuario está deshabilitado");

        verify(authenticationManager, never()).authenticate(any());
        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyMap(), any());
    }

    @Test
    void intentoDeLoginConCredencialesIncorrectas() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true);

        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any()))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessageContaining("Contraseña incorrecta");

        verify(userRepository, never()).save(any());
        verify(jwtService, never()).generateToken(anyMap(), any());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginExitoso() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");

        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true);
        user.setLastLoginAt(null);

        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("mocked-jwt-token");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));

        var response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(userRepository).save(user);

        assertNotNull(user.getLastLoginAt());

        // ✅ Verificar que la fecha esté cercana al "ahora" (máx 2 segundos de diferencia)
        assertThat(user.getLastLoginAt())
                .isCloseTo(LocalDateTime.now(), within(2, java.time.temporal.ChronoUnit.SECONDS));
    }

    @Test
    void errorGenerandoTokenJWT() {
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

        assertThatThrownBy(() -> authenticationService.login(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error generando token JWT");

        verify(userRepository).save(user);
        assertNotNull(user.getLastLoginAt());
        verify(jwtService).generateToken(anyMap(), any(User.class));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void persistenciaDelUltimoLogin() {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("info@email.com");
        request.setPassword("1234");

        User user = new User();
        user.setEmail("info@email.com");
        user.setEnabled(true);
        user.setLastLoginAt(null);

        when(userRepository.findByEmail("info@email.com"))
                .thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyMap(), any(User.class)))
                .thenReturn("mocked-jwt-token");
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList()));

        var response = authenticationService.login(request);

        assertNotNull(response);
        assertEquals("mocked-jwt-token", response.getToken());
        verify(userRepository).save(user);

        assertNotNull(user.getLastLoginAt());

        // ✅ Igual que en loginExitoso: usar closeTo
        assertThat(user.getLastLoginAt())
                .isCloseTo(LocalDateTime.now(), within(2, java.time.temporal.ChronoUnit.SECONDS));
    }
}
