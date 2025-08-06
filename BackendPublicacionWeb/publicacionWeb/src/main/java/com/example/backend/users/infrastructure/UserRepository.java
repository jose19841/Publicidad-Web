package com.example.backend.users.infrastructure;
import com.example.backend.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link User}.
 * Este repositorio proporciona métodos CRUD para interactuar con la tabla de usuarios.
 * Los métodos permiten realizar operaciones como guardar, buscar, actualizar y eliminar usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario en la base de datos por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return Un {@link Optional} que contiene el {@link User} si se encuentra un usuario con ese correo electrónico,
     *         o vacío si no se encuentra.
     */
    Optional<User> findByEmail(String email);


}
