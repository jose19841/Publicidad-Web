package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.UpdateUserUsecase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdateUser implements UpdateUserUsecase {

    private final UserRepository userRepository;

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param id          ID del usuario
     * @param userRequest DTO con los nuevos datos del usuario
     * @throws UserRegisterException si el usuario no se encuentra o el email/DNI ya están registrados
     */
    @Override
    public void execute(Long id, UserRequestDTO userRequest) {
        log.info("Iniciando actualización del usuario con ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Usuario con ID {} no encontrado en la base de datos", id);
                    return new UserRegisterException("Usuario no encontrado en la base de datos");
                });

        log.debug("Usuario encontrado: {}", user.getEmail());

        // Verifica si el email ya está en uso por otro usuario
        userRepository.findByEmail(userRequest.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    log.error("El email {} ya está registrado en otro usuario (ID: {})",
                            userRequest.getEmail(), existingUser.getId());
                    throw new UserRegisterException("El email " + userRequest.getEmail() + " ya está registrado en otro usuario.");
                });

        // Actualiza la información permitida
        log.info("Actualizando datos del usuario ID: {} -> email: {}, username: {}",
                id, userRequest.getEmail(), userRequest.getUsername());

        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setRole(Role.USER);

        userRepository.save(user);

        log.info("Usuario con ID {} actualizado exitosamente", id);
    }
}
