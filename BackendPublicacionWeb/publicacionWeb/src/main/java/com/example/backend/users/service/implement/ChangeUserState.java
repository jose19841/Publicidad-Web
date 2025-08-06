package com.example.backend.users.service.implement;

import com.example.backend.shared.exceptions.UserRegisterException;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.ChangeUserStateUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChangeUserState implements ChangeUserStateUseCase {

    private final UserRepository userRepository;


    /**
     * Cambia el estado de un usuario (ACTIVO/INACTIVO).
     *
     * @param id       ID del usuario
     * @param newState nuevo estado ("ACTIVO" o "INACTIVO")
     * @throws UserRegisterException si el estado es inválido o el usuario no se encuentra
     */
    @Override
    public void execute(Long id, String newState) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserRegisterException("Usuario no encontrado.");
        }
        User user = optionalUser.get();
        if (!"ACTIVO".equals(newState) && !"INACTIVO".equals(newState)) {
            throw new UserRegisterException("Estado inválido. Debe ser 'ACTIVO' o 'INACTIVO'.");
        }
        user.setEnabled("ACTIVO".equals(newState));
        userRepository.save(user);
    }
}
