package com.example.backend.users.service.implement;

import com.example.backend.config.security.JwtService;
import com.example.backend.shared.exceptions.DisabledUserException;
import com.example.backend.shared.exceptions.InvalidCredentialsException;
import com.example.backend.users.controller.dto.AuthenticationRequest;
import com.example.backend.users.controller.dto.AuthenticationResponse;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.usecase.AuthenticationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        log.debug("Intento de login recibido para email={}", request.getEmail());

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email={}", request.getEmail());
                    return new InvalidCredentialsException("Usuario no encontrado.");
                });

        if (!user.isEnabled()) {
            log.warn("Intento de login para usuario deshabilitado, email={}", user.getEmail());
            throw new DisabledUserException("El usuario está deshabilitado.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            log.debug("Autenticación exitosa para usuario id={}, email={}", user.getId(), user.getEmail());
        } catch (Exception e) {
            log.error("Contraseña incorrecta para usuario email={}", request.getEmail());
            throw new InvalidCredentialsException("Contraseña incorrecta.");
        }

        // Actualizar último login
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("Último login actualizado para usuario id={}, email={}", user.getId(), user.getEmail());

        Map<String, Object> claims = new HashMap<>();
        String jwt = jwtService.generateToken(claims, user);
        log.info("JWT generado exitosamente para usuario id={}", user.getId());

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
