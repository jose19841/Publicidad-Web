package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.shared.exceptions.InvalidCredentialsException;
import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.controller.dto.AuthenticationResponse;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.AuthenticationUseCase;
import com.example.backend.users.service.usecase.GetUserByEmailUseCase;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseCookie;

import java.io.InvalidClassException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @Mock
    private  AuthenticationUseCase authentication;
    @Mock
    private  CookieService cookieService;
    @Mock
    private  UserMapper userMapper;
    @Mock
    private  GetUserByEmailUseCase getUserByEmail;

    @InjectMocks
    private LoginService loginService;

    @Test
    void execute_loginCorrecto_seteaCookieYDevuelveUserDTO(){
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("joni.illes@gmail.com");
        request.setPassword("12345678");

        UserResponseDTO userLogged = new UserResponseDTO();
        userLogged.setEmail("joni.illes@gmail.com");

        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setToken("jwt-token");
        authResponse.setEmail("joni.illes@gmail.com");


        // Mocking the dependencies
        when(authentication.login(request)).thenReturn(authResponse);

        ResponseCookie cookie = ResponseCookie.from("authToken", "jwt-token").build();
        when(cookieService.createAuthCookie("jwt-token")).thenReturn(cookie);

        User user = new User();
        user.setEmail("joni.illes@gmail.com");

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        when(getUserByEmail.execute("joni.illes@gmail.com")).thenReturn(Optional.of(user));

        when(userMapper.toDTO(user)).thenReturn(userResponseDTO);


        HttpServletResponse response = mock(HttpServletResponse.class);

        // Act
        UserResponseDTO result = loginService.execute(request, response);

        // Assert
        assertEquals(userResponseDTO, result);

        verify(authentication).login(request);
        verify(cookieService).createAuthCookie("jwt-token");
        verify(response).addHeader(eq("Set-Cookie"), eq(cookie.toString()));
        verify(getUserByEmail).execute("joni.illes@gmail.com");
        verify(userMapper).toDTO(user);

    }


    @Test
    void execute_loginIncorrecto_lanzaExcepcion() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("joni.illes@hotmail.com");
        request.setPassword("wrongpassword");
        when(authentication.login(request)).thenThrow(new InvalidCredentialsException("Contraseña incorrecta"));
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Act & Assert
        assertThrows(InvalidCredentialsException.class, () -> loginService.execute(request, response));
        verify(authentication).login(request);
        verifyNoInteractions(cookieService, getUserByEmail, userMapper);
        verifyNoMoreInteractions(authentication, cookieService, getUserByEmail, userMapper);


    }
    @Test
    void execute_usuarioNoEncontrado_lanzaExcepcion() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        HttpServletResponse response = mock(HttpServletResponse.class);
        request.setEmail("joni.illes@gmail.com");
        request.setPassword("12345678");
        //act
        when(authentication.login(request)).thenThrow(new RuntimeException("Usuario no encontrado"));
        assertThrows(RuntimeException.class, () -> loginService.execute(request,response));
        verify(authentication).login(request);
        verifyNoMoreInteractions(authentication, cookieService, getUserByEmail, userMapper);
    }
    @Test
    void execute_errorEnAutenticacion_lanzaExcepcion() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("jon@gmail.com");
        request.setPassword("12345678");
        HttpServletResponse response = mock(HttpServletResponse.class);
        // Act
        when(authentication.login(request)).thenThrow(new RuntimeException("Error en autenticación"));
        // Assert
        assertThrows(RuntimeException.class, () -> loginService.execute(request, response));
        verify(authentication).login(request);
        verifyNoInteractions(cookieService, getUserByEmail, userMapper);
        verifyNoMoreInteractions(authentication, cookieService, getUserByEmail, userMapper);
    }


}