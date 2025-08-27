package com.example.backend.users.service.usecase;

public interface ResetPasswordUseCase {

     void execute(String token, String newPassword);
}
