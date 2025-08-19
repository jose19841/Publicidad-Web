package com.example.backend.users.service.mapper;

import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Convierte un UserRequestDTO a una entidad User.
     *
     * @param dto el DTO con los datos del usuario
     * @return entidad User
     */
    public User toEntity(UserRequestDTO dto ) {
        return User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .role(Role.USER)
                .enabled(true)
                .build();
    }



    /**
     * Convierte una entidad User a su representación DTO (Data Transfer Object).
     *
     * @param user la entidad User
     * @return el objeto UserResponseDTO correspondiente
     */

    public UserResponseDTO toDTO(User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getName())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .createdAt(user.getCreatedAt() != null ? user.getCreatedAt().format(formatter) : null)
                .updatedAt(user.getModifiedAt() != null ? user.getModifiedAt().format(formatter) : null)
                .lastLoginAt(user.getLastLoginAt() != null ? user.getLastLoginAt().format(formatter) : null)
                .build();
    }
}
