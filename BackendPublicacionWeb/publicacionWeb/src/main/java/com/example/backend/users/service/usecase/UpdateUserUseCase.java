package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UserRequestDTO;

public interface UpdateUserUseCase {
    void execute (Long id, UserRequestDTO userRequest);
}
