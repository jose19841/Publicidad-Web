package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UserResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface GetUserSessionUseCase {
    UserResponseDTO execute(HttpServletRequest request);
}
