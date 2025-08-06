package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UserResponseDTO;

import java.util.List;

public interface GetAllUsersUseCase {
    List<UserResponseDTO> execute();
}
