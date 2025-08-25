package com.example.backend.users.service;

import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.controller.dto.UpdateLoggedUserDTO;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.service.usecase.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios, incluyendo registro, actualización,
 * cambio de contraseña, gestión de estado, y manejo de tokens para recuperación de contraseña.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final ResetPasswordUseCase resetPassword;
    private final SendResetTokenUseCase sendResetToken;
    private final GetUserSessionUseCase getUserSession;
    private final ChangePasswordUseCase changePassword;
    private final ChangeUserStatusUseCase changeUserState;
    private final UpdateUserUsecase updateUser;
    private final GetAllUsersUseCase getAllUsers;
    private final RegisterUserUseCase registerUser;
    private final InsertAdminUserUseCase insertAdminUser;
    private final GetUserByEmailUseCase getUserByEmail;
    private final UpdateLoggedUserCase updateLoggedUser;

    public void insertAdminUser() {
        log.info("Ejecutando inserción de usuario ADMIN inicial...");
        insertAdminUser.execute();
        log.info("Usuario ADMIN insertado correctamente (si no existía).");
    }

    public void save(UserRequestDTO userRequest) {
        log.info("Registrando nuevo usuario con email: {}", userRequest.getEmail());
        registerUser.execute(userRequest);
        log.info("Usuario {} registrado correctamente.", userRequest.getEmail());
    }

    public List<UserResponseDTO> getAllUsers() {
        log.info("Obteniendo la lista completa de usuarios...");
        List<UserResponseDTO> users = getAllUsers.execute();
        log.info("Se recuperaron {} usuarios.", users.size());
        return users;
    }

    public void updateUser(Long id, UserRequestDTO userRequest) {
        log.info("Actualizando usuario con ID: {}", id);
        updateUser.execute(id, userRequest);
        log.info("Usuario con ID {} actualizado correctamente.", id);
    }

    public void changePassword(HttpServletRequest request, ChangePasswordDTO changePasswordDTO) {
        log.info("Cambio de contraseña solicitado para usuario autenticado en la sesión.");
        changePassword.execute(request, changePasswordDTO);
        log.info("Contraseña actualizada correctamente.");
    }

    public UserResponseDTO getUserSession(HttpServletRequest request) {
        log.info("Obteniendo información del usuario en sesión actual...");
        UserResponseDTO user = getUserSession.execute(request);
        log.info("Usuario en sesión recuperado: {}", user.getEmail());
        return user;
    }

    public void sendResetToken(String email) {
        log.info("Generando token de recuperación de contraseña para el email: {}", email);
        sendResetToken.execute(email);
        log.info("Token de recuperación enviado al email: {}", email);
    }

    public void resetPassword(String token, String newPassword) {
        log.info("Reseteando contraseña mediante token: {}", token);
        resetPassword.execute(token, newPassword);
        log.info("Contraseña actualizada correctamente para el token proporcionado.");
    }

    public Optional<User> getUserByEmail(String email) {
        log.info("Buscando usuario por email: {}", email);
        Optional<User> user = getUserByEmail.execute(email);
        if (user.isPresent()) {
            log.info("Usuario encontrado con email: {}", email);
        } else {
            log.warn("No se encontró usuario con email: {}", email);
        }
        return user;
    }

    public UserResponseDTO changeUserStatus(Long id) {
        log.info("Cambiando estado del usuario con ID: {}", id);
        UserResponseDTO updated = changeUserState.execute(id);
        log.info("Estado del usuario con ID {} cambiado a: {}", id, updated.isEnabled());
        return updated;
    }
    public void updateLoggedUser(UpdateLoggedUserDTO dto, String email){
        log.info("Atualizando username de usuario por email: {}", email);
       updateLoggedUser.execute(dto, email);
       log.info("Actualizacion realizada correctamente para: {}", email);
    }
}
