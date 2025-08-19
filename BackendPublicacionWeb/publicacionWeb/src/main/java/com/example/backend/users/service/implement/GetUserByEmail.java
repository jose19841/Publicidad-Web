package com.example.backend.users.service.implement;

import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.GetUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUserByEmail implements GetUserByEmailUseCase {

    private final UserRepository userRepository;

    /**
     * Busca un usuario por su dirección de email.
     *
     * @param email email del usuario
     * @return usuario encontrado (opcional)
     */
    @Override
    public Optional<User> execute(String email) {
        log.debug("Buscando usuario con email: {}", email);

        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            log.info("Usuario encontrado con email: {}", email);
        } else {
            log.warn("No se encontró usuario con email: {}", email);
        }

        return user;
    }
}
