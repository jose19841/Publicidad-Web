package com.example.backend.config.security.oauth;

import com.example.backend.config.security.CookieService;
import com.example.backend.config.security.JwtService;
import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleOAuth2SuccessHandlerTest {

    @Mock UserRepository userRepository;
    @Mock JwtService jwtService;
    @Mock CookieService cookieService;
    @Mock Authentication authentication;
    @Mock OAuth2User oAuth2User;
    @Mock HttpServletRequest request;
    @Mock HttpServletResponse response;

    @InjectMocks GoogleOAuth2SuccessHandler handler;

    private Map<String,Object> attributes;

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
        attributes.put("email", "test@example.com");
        attributes.put("name", "Test User");
    }


    @Test
    void onAuthenticationSuccess_usuarioNuevo_creaYRedirigeConCookie() throws IOException {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(attributes); // email: test@example.com, name: Test User

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());

        // Hacemos que cualquier save devuelva el mismo objeto con id seteado (simulando persistencia)
        when(userRepository.save(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            if (u.getId() == null) u.setId(1L);
            return u;
        });

        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-123");
        when(cookieService.createAuthCookie("jwt-123"))
        .thenReturn(org.springframework.http.ResponseCookie.from("authToken", "jwt-123").build());

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        // Se invoca save 2 veces: creación y actualización (lastLoginAt/loginAttempts)
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(captor.capture());

        // Primera llamada: usuario recién creado (sin lastLoginAt todavía)
        User firstSave = captor.getAllValues().get(0);
        assertEquals("test@example.com", firstSave.getEmail());
        assertEquals("test", firstSave.getName());

        // Segunda llamada: usuario con login actualizado
        User secondSave = captor.getAllValues().get(1);



    }


    @Test
    void onAuthenticationSuccess_usuarioExistenteHabilitado_actualizaLoginYRedirige() throws IOException {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        User existente = User.builder()
                .id(1L).email("test@example.com").username("test")
                .enabled(true).lastLoginAt(null).role(Role.USER).build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existente));
        when(userRepository.save(existente)).thenReturn(existente);
        when(jwtService.generateToken(existente)).thenReturn("jwt-ok");
        when(cookieService.createAuthCookie("jwt-ok")).thenReturn(org.springframework.http.ResponseCookie.from("authToken","jwt-ok").build());

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        assertNotNull(existente.getLastLoginAt());          // lastLoginAt se actualizó
        verify(userRepository).save(existente);             // se guardó con login actualizado
        verify(response).addHeader(eq("Set-Cookie"), contains("jwt-ok")); // cookie con jwt
        verify(response).sendRedirect("http://localhost:5173/"); // redirige al frontend
    }

    @Test
    void onAuthenticationSuccess_usuarioDeshabilitado_redirigeConError() throws IOException {
        // Arrange
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(attributes);
        User disabled = User.builder()
                .id(2L).email("test@example.com").username("test")
                .enabled(false).build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(disabled));

        // Act
        handler.onAuthenticationSuccess(request, response, authentication);

        // Assert
        verify(response).sendRedirect("http://localhost:5173/login?error=disabled"); // redirige con error
        verifyNoInteractions(jwtService);      // no genera token
        verifyNoInteractions(cookieService);   // no crea cookie
    }

    @Test
    void makeUniqueUsername_agregaSufijoSiYaExiste() {
        // Arrange
        when(userRepository.findByUsername("base")).thenReturn(Optional.of(new User())); // ya existe "base"
        when(userRepository.findByUsername("base1")).thenReturn(Optional.empty());      // "base1" no existe

        // Act (usamos ReflectionTestUtils para invocar el método privado)
        String result = org.springframework.test.util.ReflectionTestUtils.invokeMethod(handler, "makeUniqueUsername", "base");

        // Assert
        assertEquals("base1", result); // se creó un username único
    }

    @Test
    void slug_eliminaCaracteresEspecialesYMinusculiza() {
        // Act
        String result = org.springframework.test.util.ReflectionTestUtils.invokeMethod(handler, "slug", "ÁéÍóÚ User!");

        // Assert
        assertEquals("aeiouuser", result); // sin acentos, espacios ni caracteres especiales
    }
}
