package com.example.backend.users.service;


import com.example.backend.users.controller.dto.ChangePasswordDTO;
import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.controller.dto.UserRequestDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.service.usecase.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con los usuarios, incluyendo registro, actualización,
 * cambio de contraseña, gestión de estado, y manejo de tokens para recuperación de contraseña.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final ResetPasswordUseCase resetPassword;
    private final SendResetTokenUseCase sendResetToken;
    private final GetUserSessionUseCase getUserSession;
    private final ChangePasswordUseCase changePassword;
    private final ChangeUserStatusUseCase changeUserState;
    private final UpdateUserUseCase updateUser;
    private final GetAllUsersUseCase getAllUsers;
    private final RegisterUserUseCase registerUser;
    private final InsertAdminUserUseCase insertAdminUser;
    private final GetUserByEmailUseCase getUserByEmail;


    public void insertAdminUser() { insertAdminUser.execute();}

    public void save(UserRequestDTO userRequest) {registerUser.execute(userRequest);}

    public List<UserResponseDTO> getAllUsers() { return getAllUsers.execute();}

    public void updateUser(Long id, UserRequestDTO userRequest) { updateUser.execute(id,userRequest);}

    public void changePassword(HttpServletRequest request, ChangePasswordDTO changePasswordDTO) { changePassword.execute(request,changePasswordDTO); }

    public UserResponseDTO getUserSession(HttpServletRequest request) { return getUserSession.execute(request);}

    public void sendResetToken(String email) {sendResetToken.execute(email); }

    public void resetPassword(String token, String newPassword) {
        resetPassword.execute( token, newPassword);
    }

    public Optional<User> getUserByEmail(String email){return getUserByEmail.execute(email) ;}

    public UserResponseDTO changeUserStatus(Long id) { return changeUserState.execute(id); }
}
