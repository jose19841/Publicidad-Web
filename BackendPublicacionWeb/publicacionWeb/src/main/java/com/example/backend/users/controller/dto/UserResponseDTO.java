package com.example.backend.users.controller.dto;

import com.example.backend.users.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String email;
    private String username;
    private Role role;
    private boolean enabled;
    private String createdAt;
    private String updatedAt;
    private String lastLoginAt;
}