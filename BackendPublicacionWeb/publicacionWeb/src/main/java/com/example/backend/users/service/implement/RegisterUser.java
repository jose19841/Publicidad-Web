package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUser implements RegisterUserUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param userRequest DTO con los datos del nuevo usuario
     * @throws UserRegisterException si el email o el documento ya están registrados
     */
    @Override
    public void execute(UserRequestDTO userRequest) {
        // Validar email repetido
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new UserRegisterException("El usuario con email " + userRequest.getEmail() + " ya está registrado.");
        }

        // Validar username repetido
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new UserRegisterException("El nombre de usuario " + userRequest.getUsername() + " ya está registrado.");
        }

        // Crear usuario nuevo y guardar
        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

}
