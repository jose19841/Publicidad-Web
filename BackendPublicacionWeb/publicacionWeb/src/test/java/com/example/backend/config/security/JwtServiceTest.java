package com.example.backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class JwtServiceTest {


    @Mock
    private UserDetails userDetails;


    private JwtService jwtService;
    @BeforeEach
    void setUp() {
        jwtService = new JwtService();


        // Seteamos el valor del campo privado "secretKey" con reflection
        ReflectionTestUtils.setField(jwtService, "secretKey",
                "1s11asde125cfd4683b7ab38f3345aceeb22698d97ebeaa1z4dj1ee2c2ed4116");

        // Creamos un usuario simulado
        userDetails = new User(
                "usuarioPrueba",           // username
                "password",                // password
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")) // rol
        );
    }

    @Test
    void generateToken_y_extractUserName_devuelveMismoUsuario() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // genera un JWT válido con el username y rol

        // Act
        String username = jwtService.extractUserName(token);
        // extrae el claim "subject" (username) del JWT

        // Assert
        assertEquals(userDetails.getUsername(), username);
        // comprobamos que el username extraído del token es el mismo que el del UserDetails
    }

    @Test
    void isTokenValid_conTokenValido_devuelveTrue() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // generamos un token válido

        // Act
        boolean valido = jwtService.isTokenValid(token, userDetails);
        // valida que el usuario coincida y que no esté expirado

        // Assert
        assertTrue(valido);
    }

    @Test
    void extractClaim_extraeFechaExpiracion() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // generamos un token válido

        // Act
        Date expiration = jwtService.extractClaim(token, Claims::getExpiration);
        // usamos extractClaim para obtener la fecha de expiración del token

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
        // la fecha de expiración debe ser futura
    }

    @Test
    void generateToken_conClaimsAdicionales_incluyeEsosDatos() {
        // Arrange
        HashMap<String, Object> extras = new HashMap<>();
        extras.put("custom", "valorExtra");

        String token = jwtService.generateToken(extras, userDetails);
        // generamos un token con claims adicionales

        // Act
        String valor = jwtService.extractClaim(token, claims -> claims.get("custom", String.class));
        // extraemos el claim "custom"

        // Assert
        assertEquals("valorExtra", valor);
    }

    @Test
    void isTokenValid_conTokenDeOtroUsuario_devuelveFalse() {
        // Arrange
        String token = jwtService.generateToken(userDetails);
        // token válido para "usuarioPrueba"

        UserDetails otroUsuario = new User(
                "otroUsuario",
                "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Act
        boolean valido = jwtService.isTokenValid(token, otroUsuario);
        // validamos el token pero contra otro usuario

        // Assert
        assertFalse(valido);
    }
}
