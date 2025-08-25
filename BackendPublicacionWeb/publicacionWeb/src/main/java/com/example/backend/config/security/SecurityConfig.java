package com.example.backend.config.security;

import com.example.backend.config.security.oauth.GoogleOAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

/**
 * Configuración de seguridad para la aplicación.
 * Aquí se definen las reglas de acceso a las rutas y cómo manejar la autenticación y autorización.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${security.public-paths}")
    private String[] publicPaths;

    @Value("${security.cors.allowed-origins}")
    private String[] allowedOrigins;

    private final JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;  // Filtro para manejar la autenticación mediante cookies.
    private final AuthenticationProvider authenticationProvider;  // Proveedor de autenticación.
    private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler; // 🔹 Inyectamos el handler

    /**
     * Configura el filtrado de seguridad para la aplicación web.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of(allowedOrigins));

                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    corsConfiguration.addAllowedHeader("*");
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtCookieAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicPaths).permitAll()
                        .anyRequest().authenticated()
                )

                // 🔹 Esto evita la redirección a Google y devuelve 401 en API
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // 🔹 Configuración para Google OAuth2
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(googleOAuth2SuccessHandler)
                );

        return http.build();
    }

}
