package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.ChangePasswordDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface ChangePasswordUseCase {

    void execute (HttpServletRequest request, ChangePasswordDTO changePasswordDTO);
}
