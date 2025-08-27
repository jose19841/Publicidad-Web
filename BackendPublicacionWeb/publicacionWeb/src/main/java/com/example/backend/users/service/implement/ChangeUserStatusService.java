package com.example.backend.users.service.implement;

import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.ChangeUserStatusUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeUserStatusService implements ChangeUserStatusUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO execute(Long id) {
        log.debug("Iniciando cambio de estado para usuario con id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se encontró el usuario con id={}", id);
                    return new RuntimeException("Usuario no encontrado");
                });

        boolean previousStatus = user.isEnabled();
        user.setEnabled(!previousStatus);

        log.info("Cambiando estado del usuario con id={} de {} a {}",
                id, previousStatus, user.isEnabled());

        User saved = userRepository.save(user);

        log.debug("Usuario con id={} actualizado correctamente en la base de datos", id);

        return userMapper.toDTO(saved);
    }
}
