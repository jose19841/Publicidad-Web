package com.example.backend.users.service.implement;

import com.example.backend.config.security.CookieService;
import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangePasswordTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @Mock
    private  CookieService cookieService;
    @InjectMocks
    private  ChangePassword changePassword;

    @Test
    void execute_usuarioNoAutenticado_lanzaExcepcion() {

        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();

        when(cookieService.getUserFromCookie(request)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RuntimeException.class, () -> changePassword.execute(request, changePasswordDTO));

        // Verificaciones: que no se llame al repo ni al encoder
        verifyNoInteractions(userRepository, passwordEncoder);
    }


    @Test
    void execute_contrasenaActualIncorrecta_lanzaExcepcion(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setCurrentPassword("currentPassword");
        changePasswordDTO.setRepeatPassword("repeatPassword");

        User user = new User();

        when(cookieService.getUserFromCookie(request)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(false);
        user.setPassword("encodedPassword");
        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> changePassword.execute(request, changePasswordDTO));
        assertEquals("La contraseña actual es incorrecta", ex.getMessage());
        verify(passwordEncoder).matches("currentPassword", "encodedPassword");
        verifyNoMoreInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
    }
    @Test
    void execute_nuevasContrasenasNoCoinciden_lanzaExcepcion()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setCurrentPassword("currentPassword");
        changePasswordDTO.setRepeatPassword("repeatPassword");

        User user = new User();
        when(cookieService.getUserFromCookie(request)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("currentPassword", "encodedPassword")).thenReturn(true);
        user.setPassword("encodedPassword");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> changePassword.execute(request, changePasswordDTO));
        assertEquals("Las nuevas contraseñas no coinciden", ex.getMessage());

        verify(userRepository , never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());


    }
    @Test
    void execute_nuevaContrasenaIgualALaActual_lanzaExcepcion(){
        HttpServletRequest request = mock(HttpServletRequest.class);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setCurrentPassword("newPassword");
        changePasswordDTO.setRepeatPassword("newPassword");

        User user = new User();
        user.setPassword("encodedPassword");
        when(cookieService.getUserFromCookie(request)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("newPassword", "encodedPassword")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> changePassword.execute(request, changePasswordDTO));
        assertEquals("La nueva contraseña no puede ser igual a la actual", ex.getMessage());


        verify(userRepository , never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());

    }
    @Test
    void execute_cambioExitoso_guardaUsuarioConNuevaContrasena()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setNewPassword("newPassword");
        changePasswordDTO.setRepeatPassword("newPassword");
        changePasswordDTO.setCurrentPassword("currentPassword");


        User user = new User();
        user.setPassword("currentPassword");
        when(cookieService.getUserFromCookie(request)).thenReturn(Optional.of(user));

        when(passwordEncoder.matches("currentPassword", "currentPassword")).thenReturn(true); // <-- faltaba
        when(passwordEncoder.matches("newPassword", "currentPassword")).thenReturn(false);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        changePassword.execute(request, changePasswordDTO);
        assertEquals("newEncodedPassword", user.getPassword());
        verify(userRepository).save(user);
        verify(passwordEncoder).encode("newPassword");

    }
}