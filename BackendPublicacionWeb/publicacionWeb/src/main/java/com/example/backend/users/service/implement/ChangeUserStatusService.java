package com.example.backend.users.service.implement;

import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.ChangeUserStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeUserStatusService implements ChangeUserStatusUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO execute(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setEnabled(!user.isEnabled());
        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }
}
