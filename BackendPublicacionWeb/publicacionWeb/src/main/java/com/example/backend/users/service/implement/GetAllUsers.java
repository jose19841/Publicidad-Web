package com.example.backend.users.service.implement;

import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.GetAllUsersUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllUsers implements GetAllUsersUseCase {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return lista de usuarios en formato UserDTO
     */
    @Override
    public List<UserResponseDTO> execute() {
        List<User> usersList = userRepository.findAll();
        return usersList.stream().map(userMapper::toDTO).collect(Collectors.toList());
    }
}

