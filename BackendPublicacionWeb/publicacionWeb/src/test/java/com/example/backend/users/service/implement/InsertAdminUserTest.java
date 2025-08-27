package com.example.backend.users.service.implement;

import com.example.backend.users.domain.Role;
import com.example.backend.users.domain.User;
import com.example.backend.users.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InsertAdminUserTest {
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;
    @InjectMocks
    private InsertAdminUser insertAdminUser;

    @Test
    void execute_adminNoExiste_creaNuevoAdmin (){
        when(userRepository.findByEmail("admin@admin.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("admin123")).thenReturn("encodedPass");
        insertAdminUser.execute();
        verify(userRepository).save(any(User.class));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals("admin@admin.com", savedUser.getEmail());
        assertEquals("admin", savedUser.getName());
        assertEquals(Role.ADMIN, savedUser.getRole());
        assertTrue(savedUser.isEnabled());
        assertEquals("encodedPass", savedUser.getPassword());

        verify(passwordEncoder).encode("admin123");

    }
    @Test
    void execute_adminYaExiste_noHaceNada(){
        when(userRepository.findByEmail("admin@admin.com")).thenReturn(Optional.of(new User()));
        insertAdminUser.execute();
        verify(userRepository, never()).save(any(User.class));
    }

}