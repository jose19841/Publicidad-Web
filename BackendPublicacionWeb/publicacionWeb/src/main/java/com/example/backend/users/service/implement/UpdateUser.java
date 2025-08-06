package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.UpdateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UpdateUser implements UpdateUserUseCase {

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
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserRegisterException("Usuario no encontrado en la base de datos"));

        // Verifica si el email ya está en uso por otro usuario
        userRepository.findByEmail(userRequest.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new UserRegisterException("El email " + userRequest.getEmail() + " ya está registrado en otro usuario.");
                });

        // Actualiza la información permitida
        user.setEmail(userRequest.getEmail());
        user.setUsername(userRequest.getUsername());
        user.setRole(userRequest.getRole());

        // updatedAt se actualiza automáticamente con @PreUpdate en la entidad
        userRepository.save(user);

    }
}
