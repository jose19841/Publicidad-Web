package com.example.backend.users.service.mapper;

import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper mapper;

    @Test
    void toEntity_convierteCorrectamente(){
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("test@mail.com");
        dto.setUsername("tester");
        dto.setPassword("secret");
        User user = mapper.toEntity(dto);
        assertThat(user.getEmail()).isEqualTo("test@mail.com");
        assertThat(user.getName()).isEqualTo("tester");
        assertThat(user.getPassword()).isEqualTo("secret");
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.isEnabled()).isTrue();
    }

    @Test
    void toDTO_convierteCorrectamente(){
        User user = User.builder()
                .id(1L)
                .email("dto@mail.com")
                .username("dtoUser")
                .role(Role.ADMIN)
                .enabled(false)
                .lastLoginAt(LocalDateTime.of(2025, 1, 3, 12, 50))
                .build();

        UserResponseDTO dto = mapper.toDTO(user);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getEmail()).isEqualTo("dto@mail.com");
        assertThat(dto.getUsername()).isEqualTo("dtoUser");
        assertThat(dto.getRole()).isEqualTo(Role.ADMIN);
        assertThat(dto.isEnabled()).isFalse();
        assertThat(dto.getLastLoginAt()).isEqualTo("03/01/2025 12:50");
    }

    @Test
    void toDTOConFechasNulas(){
        User user = User.builder()
                .id(2L)
                .email("null@mail.com")
                .username("nullUser")
                .role(Role.USER)
                .enabled(true)
                .lastLoginAt(null)
                .build();
        UserResponseDTO dto = mapper.toDTO(user);
        assertThat(dto.getCreatedAt()).isNull();
        assertThat(dto.getUpdatedAt()).isNull();
        assertThat(dto.getLastLoginAt()).isNull();
    }
}