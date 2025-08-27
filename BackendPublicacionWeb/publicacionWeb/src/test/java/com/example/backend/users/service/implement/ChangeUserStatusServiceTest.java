package com.example.backend.users.service.implement;

import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeUserStatusServiceTest {

    @Mock
    private  UserRepository userRepository;
    @Mock
    private  UserMapper userMapper;
    @InjectMocks
    private ChangeUserStatusService changeUserStatusService;

    @Test
    void execute_usuarioHabilitado_loDeshabilitaYDevuelveDTOCorrecto() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEnabled(true);

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setEnabled(false);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO expectedDTO = new UserResponseDTO();
        expectedDTO.setId(userId);
        expectedDTO.setEnabled(false);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDTO);

        // Act
        UserResponseDTO result = changeUserStatusService.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).findById(userId);
        verify(userRepository).save(captor.capture());
        verify(userMapper).toDTO(savedUser);
        verifyNoMoreInteractions(userRepository, userMapper);

        assertFalse(captor.getValue().isEnabled(), "El usuario debe guardarse deshabilitado");
    }


    @Test
    void execute_usuarioDeshabilitado_loHabilita_guardaYMapea(){
        // Arrange
        Long userId = 2L;
        User user = new User();
        user.setId(userId);
        user.setEnabled(false);

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setEnabled(true);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO expectedDTO = new UserResponseDTO();
        expectedDTO.setId(userId);
        expectedDTO.setEnabled(true);
        when(userMapper.toDTO(savedUser)).thenReturn(expectedDTO);

        // Act
        UserResponseDTO result = changeUserStatusService.execute(userId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).findById(userId);
        verify(userRepository).save(captor.capture());
        verify(userMapper).toDTO(savedUser);
        verifyNoMoreInteractions(userRepository, userMapper);

        assertTrue(captor.getValue().isEnabled(), "El usuario debe guardarse habilitado");

    }

    @Test
    void execute_usuarioNoEncontrado_lanzaExcepcion() {
        // Arrange
        Long userId = 3L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            changeUserStatusService.execute(userId);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(userRepository).findById(userId);
        verifyNoMoreInteractions(userRepository, userMapper);
        verifyNoInteractions(userMapper);

    }
}