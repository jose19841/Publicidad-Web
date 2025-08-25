package com.example.backend.users.service.implement;

import com.example.backend.users.service.usecase.LogoutUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogoutService implements LogoutUseCase {

    @Override
    public void execute(HttpServletResponse response) {
        log.info("Iniciando proceso de logout: invalidando cookie de autenticación.");

        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        log.info("Cookie de autenticación eliminada correctamente. Logout exitoso.");
    }
}
