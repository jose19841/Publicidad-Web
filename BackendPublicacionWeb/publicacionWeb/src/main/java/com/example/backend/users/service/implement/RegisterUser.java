package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.RegisterUserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
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
     * @throws UserRegisterException si el email o el username ya están registrados
     */
    @Override
    public void execute(UserRequestDTO userRequest) {
        log.info("Iniciando registro de usuario con email: {} y username: {}",
                userRequest.getEmail(), userRequest.getUsername());

        // Validar email repetido
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            log.warn("Intento de registro fallido: email ya registrado -> {}", userRequest.getEmail());
            throw new UserRegisterException("El usuario con email " + userRequest.getEmail() + " ya está registrado.");
        }

        // Validar username repetido
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            log.warn("Intento de registro fallido: username ya registrado -> {}", userRequest.getUsername());
            throw new UserRegisterException("El nombre de usuario " + userRequest.getUsername() + " ya está registrado.");
        }

        // Crear usuario nuevo
        User user = userMapper.toEntity(userRequest);
        log.debug("Usuario mapeado desde DTO: {}", user);

        // Encriptar contraseña
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.debug("Contraseña encriptada correctamente para usuario con email: {}", user.getEmail());

        // Guardar en BD
        userRepository.save(user);
        log.info("Usuario registrado exitosamente con ID: {} y email: {}", user.getId(), user.getEmail());
    }
}
