package com.example.backend.users.service.implement;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UpdateUserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpdateUser updateUser;

    @Test
    void actualizaUsuarioCorrectamente() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("viejo@mail.com");
        existingUser.setUsername("viejoUser");

        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("nuevo@mail.com");
        request.setUsername("nuevoUser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("nuevo@mail.com")).thenReturn(Optional.empty());

        // Act
        updateUser.execute(userId, request);

        // Assert
        assertThat(existingUser.getEmail()).isEqualTo("nuevo@mail.com");
        assertThat(existingUser.getName()).isEqualTo("nuevoUser");
        verify(userRepository).save(existingUser);
    }

    @Test
    void lanzaExcepcionSiUsuarioNoExiste() {
        // Arrange
        Long userId = 1L;
        UserRequestDTO request = new UserRequestDTO();
        request. setEmail("nuevo@mail.com");
        request.setUsername("nuevoUser");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act + Assert
        assertThatThrownBy(() -> updateUser.execute(userId, request))
                .isInstanceOf(UserRegisterException.class)
                .hasMessageContaining("Usuario no encontrado");

        verify(userRepository, never()).save(any());
    }


    @Test
    void lanzaExcepcionSiEmailYaRegistradoPorOtroUsuario(){
            // Arrange
            Long userId = 1L;
            User existingUser = new User();
            existingUser.setId(userId);
            existingUser.setEmail("viejo@mail.com");
            existingUser.setUsername("viejoUser");

            User otroUsuario = new User();
            otroUsuario.setId(2L);
            otroUsuario.setEmail("nuevo@mail.com");

            UserRequestDTO request = new UserRequestDTO();
            request.setEmail("nuevo@mail.com");
            request.setUsername("nuevoUser");

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(userRepository.findByEmail("nuevo@mail.com")).thenReturn(Optional.of(otroUsuario));

            // Act + Assert
            assertThatThrownBy(() -> updateUser.execute(userId, request))
                    .isInstanceOf(UserRegisterException.class)
                    .hasMessageContaining("ya está registrado");

            verify(userRepository, never()).save(any());



    }

}