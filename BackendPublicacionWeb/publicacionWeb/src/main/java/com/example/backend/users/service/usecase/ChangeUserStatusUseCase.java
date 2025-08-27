package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UserResponseDTO;

public interface ChangeUserStatusUseCase {

    public UserResponseDTO execute(Long id);
}
