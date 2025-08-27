package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UpdateLoggedUserDTO;
import com.example.backend.users.controller.dto.UserRequestDTO;

public interface UpdateUserUsecase {
    void execute (Long id, UserRequestDTO userRequest);
}
