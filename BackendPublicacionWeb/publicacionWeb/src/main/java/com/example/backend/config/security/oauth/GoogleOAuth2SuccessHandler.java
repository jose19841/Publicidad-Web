package com.example.backend.config.security.oauth;

import com.example.backend.shared.exceptions.DisabledUserException;
import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.config.security.JwtService;
import com.example.backend.config.security.CookieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.text.Normalizer;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final CookieService cookieService;

    // Cambiá si tu frontend usa otro puerto/path
    private static final String FRONTEND_URL = "http://localhost:5173";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req,
                                        HttpServletResponse res,
                                        Authentication authentication) throws IOException {
        log.info("✅ Inicio de autenticación con Google OAuth2");

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();
        log.debug("Atributos recibidos de Google: {}", attributes);

        String email = (String) attributes.get("email");
        String name  = (String) attributes.getOrDefault("name",
                (String) attributes.getOrDefault("given_name", "Google User"));

        log.info("📧 Usuario autenticado por Google con email: {}", email);

        // 1) Crear si no existe
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            log.info("Usuario con email {} no existe en DB, creando nuevo...", email);
            String baseUsername = email != null ? email.split("@")[0] : name;
            String username = makeUniqueUsername(slug(baseUsername));

            User u = User.builder()
                    .email(email)
                    .username(username)
                    .password("{noop}" + UUID.randomUUID()) // nunca se usa para login
                    .role(Role.USER)
                    .enabled(true)
                    .accountLocked(false)
                    .loginAttempts(0)
                    .build();

            log.info("Nuevo usuario creado con username: {}", username);
            return userRepository.save(u);
        });

        // 🚨 VALIDAR ESTADO
        if (!user.isEnabled()) {
            log.warn("❌ Usuario {} está deshabilitado. Redirigiendo a frontend con error...", user.getEmail());
            res.sendRedirect(FRONTEND_URL + "/login?error=disabled");
            return;
        }

        // 2) Marcar último login
        user.setLastLoginAt(LocalDateTime.now());
        user.setLoginAttempts(0);
        userRepository.save(user);
        log.info("🕒 Último login actualizado para usuario: {}", user.getEmail());

        // 3) Emitir JWT y ponerlo en cookie
        String jwt = jwtService.generateToken(user);
        var cookie = cookieService.createAuthCookie(jwt);
        res.addHeader("Set-Cookie", cookie.toString());
        log.info("🔑 JWT generado y agregado en cookie para usuario {}", user.getEmail());

        // 4) Redirigir al frontend ya logueado
        res.sendRedirect(FRONTEND_URL + "/");
        log.info("✅ Autenticación exitosa, redirigiendo al frontend");
    }

    // Helpers -----------------------------

    private String slug(String s) {
        String n = Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return n.toLowerCase().replaceAll("[^a-z0-9_\\-]", "");
    }

    private String makeUniqueUsername(String base) {
        String candidate = base;
        int i = 0;
        while (userRepository.findByUsername(candidate).isPresent()) {
            i++;
            candidate = base + i;
        }
        log.debug("Generado username único: {}", candidate);
        return candidate;
    }
}
