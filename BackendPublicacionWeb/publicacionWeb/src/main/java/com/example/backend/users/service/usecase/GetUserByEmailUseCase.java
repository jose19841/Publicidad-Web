package com.example.backend.users.service.usecase;

import com.example.backend.users.domain.User;

import java.util.Optional;

public interface GetUserByEmailUseCase {
    Optional<User> execute(String email);

}
