package com.example.backend.config.security;

import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CookieServiceTest {

    @Mock JwtService jwtService;
    @Mock UserRepository userRepository;
    @Mock HttpServletRequest request;

    @InjectMocks CookieService cookieService;

    @BeforeEach
    void setUp() {
        // Seteamos los @Value privados del servicio para poder verificarlos en createAuthCookie(...)
        ReflectionTestUtils.setField(cookieService, "secure", true);
        ReflectionTestUtils.setField(cookieService, "sameSite", "Strict");
        ReflectionTestUtils.setField(cookieService, "path", "/");
        ReflectionTestUtils.setField(cookieService, "maxAge", 3600L);
    }

    @Test
    void createAuthCookie_aplicaConfiguracionYToken() {
        // Arrange: definimos un JWT de ejemplo
        String jwt = "jwt-ejemplo-123";

        // Act: creamos la cookie de autenticación usando el servicio
        ResponseCookie cookie = cookieService.createAuthCookie(jwt);

        // Assert: verificamos nombre/valor y todas las flags configuradas
        assertEquals("authToken", cookie.getName());        // la cookie se llama "authToken"
        assertEquals(jwt, cookie.getValue());               // el valor es el JWT enviado
        assertTrue(cookie.isHttpOnly());                    // debe ser httpOnly
        assertTrue(cookie.isSecure());                      // secure = true (según @Value)
        assertEquals("Strict", cookie.getSameSite());       // sameSite = "Strict"
        assertEquals("/", cookie.getPath());                // path = "/"
        assertEquals(3600, cookie.getMaxAge().getSeconds());// maxAge = 3600s
    }

    @Test
    void getUserFromCookie_sinCookies_devuelveEmpty() {
        // Arrange: la request no trae cookies
        when(request.getCookies()).thenReturn(null);

        // Act: intentamos extraer usuario desde la cookie
        Optional<User> result = cookieService.getUserFromCookie(request);

        // Assert: no hay token -> vacío, sin interacciones con jwtService/userRepository
        assertTrue(result.isEmpty());
        verifyNoInteractions(jwtService, userRepository);
    }

    @Test
    void getUserFromCookie_conCookiesPeroSinAuthToken_devuelveEmpty() {
        // Arrange: hay cookies, pero ninguna se llama "authToken"
        Cookie[] cookies = { new Cookie("otra", "x"), new Cookie("foo", "bar") };
        when(request.getCookies()).thenReturn(cookies);

        // Act: intentamos extraer usuario
        Optional<User> result = cookieService.getUserFromCookie(request);

        // Assert: no encuentra el token -> vacío, sin llamar a servicios
        assertTrue(result.isEmpty());
        verifyNoInteractions(jwtService, userRepository);
    }

    @Test
    void getUserFromCookie_conAuthToken_peroUsernameNull_devuelveEmpty() {
        // Arrange: viene authToken en las cookies
        Cookie[] cookies = { new Cookie("authToken", "token-123") };
        when(request.getCookies()).thenReturn(cookies);
        // ...pero el JwtService no logra extraer un username válido
        when(jwtService.extractUserName("token-123")).thenReturn(null);

        // Act: intentamos extraer usuario
        Optional<User> result = cookieService.getUserFromCookie(request);

        // Assert: como no hay username, no se consulta al repo y se devuelve vacío
        assertTrue(result.isEmpty());
        verify(jwtService).extractUserName("token-123");
        verifyNoInteractions(userRepository);
    }

    @Test
    void getUserFromCookie_conAuthToken_yUsuarioNoExiste_devuelveEmpty() {
        // Arrange: cookie válida con token
        Cookie[] cookies = { new Cookie("authToken", "token-xyz") };
        when(request.getCookies()).thenReturn(cookies);
        // JwtService extrae el username del token
        when(jwtService.extractUserName("token-xyz")).thenReturn("user@test.com");
        // El repositorio no encuentra al usuario
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.empty());

        // Act: obtenemos usuario desde la cookie
        Optional<User> result = cookieService.getUserFromCookie(request);

        // Assert: no existe el usuario -> Optional vacío
        assertTrue(result.isEmpty());
        verify(jwtService).extractUserName("token-xyz");
        verify(userRepository).findByEmail("user@test.com");
    }

    @Test
    void getUserFromCookie_happyPath_devuelveUsuario() {
        // Arrange: cookie con authToken presente
        Cookie[] cookies = { new Cookie("authToken", "token-ok") };
        when(request.getCookies()).thenReturn(cookies);
        // JwtService logra extraer el username
        when(jwtService.extractUserName("token-ok")).thenReturn("ok@test.com");
        // El repositorio encuentra al usuario
        User user = new User();
        user.setId(1L);
        user.setEmail("ok@test.com");
        when(userRepository.findByEmail("ok@test.com")).thenReturn(Optional.of(user));

        // Act: extraemos el usuario desde la cookie
        Optional<User> result = cookieService.getUserFromCookie(request);

        // Assert: el usuario está presente y corresponde al del repositorio
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("ok@test.com", result.get().getEmail());
        verify(jwtService).extractUserName("token-ok");
        verify(userRepository).findByEmail("ok@test.com");
    }
}
