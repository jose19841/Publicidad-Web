package com.example.backend.users.service.implement;

import com.example.backend.users.controller.dto.UserResponseDTO;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import com.example.backend.users.service.mapper.UserMapper;
import com.example.backend.users.service.usecase.GetAllUsersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        log.debug("Iniciando búsqueda de todos los usuarios...");

        List<User> usersList = userRepository.findAll();

        log.info("Se encontraron {} usuarios en la base de datos", usersList.size());

        List<UserResponseDTO> dtoList = usersList.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        log.debug("Transformación a DTOs completada. Total DTOs: {}", dtoList.size());

        return dtoList;
    }
}
