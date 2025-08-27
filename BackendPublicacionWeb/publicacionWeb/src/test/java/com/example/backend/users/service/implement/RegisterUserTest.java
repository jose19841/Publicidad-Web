package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUser registerUser; // clase a testear

    @Test
    void registraUsuarioCuandoNoHayDuplicados_yEncriptaPassword() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("nuevo@mail.com");
        dto.setUsername("nuevoUser");
        dto.setPassword("plainPass");

        User mapped = new User();
        mapped.setEmail(dto.getEmail());
        mapped.setUsername(dto.getUsername());
        mapped.setPassword(dto.getPassword()); // antes de encriptar

        when(userRepository.findByEmail("nuevo@mail.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("nuevoUser")).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(mapped);
        when(passwordEncoder.encode("plainPass")).thenReturn("ENC(plainPass)");

        // Act
        registerUser.execute(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertThat(saved.getEmail()).isEqualTo("nuevo@mail.com");
        assertThat(saved.getName()).isEqualTo("nuevoUser");
        assertThat(saved.getPassword()).isEqualTo("ENC(plainPass)"); // verificación clave

    }

    @Test
    void lanzaExcepcionSiEmailYaExiste() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("repetido@mail.com");
        dto.setUsername("userX");
        dto.setPassword("pass");

        when(userRepository.findByEmail("repetido@mail.com"))
                .thenReturn(Optional.of(new User())); // email ya en uso

        // Act + Assert
        assertThatThrownBy(() -> registerUser.execute(dto))
                .isInstanceOf(UserRegisterException.class)
                .hasMessageContaining("ya está registrado");

        verify(userRepository, never()).save(any()); // nunca debe intentar guardar
    }

    @Test
    void lanzaExcepcionSiUserNameYaExiste() {
        // Arrange
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("repetido@mail.com");
        dto.setUsername("userX");
        dto.setPassword("pass");

        when(userRepository.findByUsername("userX"))
                .thenReturn(Optional.of(new User())); // email ya en uso

        // Act + Assert
        assertThatThrownBy(() -> registerUser.execute(dto))
                .isInstanceOf(UserRegisterException.class)
                .hasMessageContaining("ya está registrado");

        verify(userRepository, never()).save(any()); // nunca debe intentar guardar
    }

}