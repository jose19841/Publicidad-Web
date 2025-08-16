package com.example.backend.users.infrastructure;

import com.example.backend.users.domain.ResetToken;
import com.example.backend.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link ResetToken}.
 * Este repositorio proporciona métodos CRUD para interactuar con la tabla de los tokens de restablecimiento de contraseña.
 * Los tokens son utilizados para verificar la validez de las solicitudes de restablecimiento de contraseñas de los usuarios.
 */
@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    /**
     * Busca un token de restablecimiento por su valor.
     *
     * @param token El token de restablecimiento a buscar.
     * @return Un {@link Optional} que contiene el {@link ResetToken} si se encuentra en la base de datos, o vacío si no se encuentra.
     */
    Optional<ResetToken> findByToken(String token);

    void deleteByUser(User user);

    Optional<ResetToken> findByUser(User user);
}
