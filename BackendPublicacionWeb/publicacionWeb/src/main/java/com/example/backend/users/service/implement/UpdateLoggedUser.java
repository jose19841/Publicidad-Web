package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.controller.dto.UpdateLoggedUserDTO;

import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.UpdateLoggedUserCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UpdateLoggedUser implements UpdateLoggedUserCase {

    private final UserRepository userRepository;


    @Override
    public void execute(UpdateLoggedUserDTO dto, String email) {
        log.info("Actualizando username para el mail: {}", email);

        // Buscar el usuario por el email obtenido de la sesión
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserRegisterException("Usuario no encontrado con el mail especificado."));

        // Establecer el nuevo nombre de usuario
        user.setUsername(dto.getUsername());

        // Guardar los cambios
        userRepository.save(user);

        log.info("Username actualizado correctamente para email: {}", email);
    }
}