package com.example.backend.users.service.usecase;

import com.example.backend.users.controller.dto.UpdateLoggedUserDTO;
import com.example.backend.users.controller.dto.UserRequestDTO;

public interface UpdateLoggedUserCase {
    void execute (UpdateLoggedUserDTO dto, String email);
}
