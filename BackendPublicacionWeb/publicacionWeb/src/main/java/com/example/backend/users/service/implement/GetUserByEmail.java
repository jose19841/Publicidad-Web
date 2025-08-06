package com.example.backend.users.service.implement;

import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.GetUserByEmailUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        return userRepository.findByEmail(email);
    }
}
