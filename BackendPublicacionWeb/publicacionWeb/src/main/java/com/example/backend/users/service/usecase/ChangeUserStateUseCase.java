package com.example.backend.users.service.usecase;

public interface ChangeUserStateUseCase {

    void execute (Long id, String newState);
}
