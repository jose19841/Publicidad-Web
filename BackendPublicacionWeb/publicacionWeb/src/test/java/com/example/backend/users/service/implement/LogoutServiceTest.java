package com.example.backend.users.service.implement;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LogoutServiceTest {


        private final LogoutService logoutService = new LogoutService();

        @Test
        void execute_agregaCookieDeLogout() {
            // Arrange
            HttpServletResponse response = mock(HttpServletResponse.class);
            ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);

            // Act
            logoutService.execute(response);

            // Assert
            verify(response).addCookie(cookieCaptor.capture());
            Cookie cookie = cookieCaptor.getValue();

            assertEquals("authToken", cookie.getName());
            assertNull(cookie.getValue());
            assertEquals(0, cookie.getMaxAge());
            assertEquals("/", cookie.getPath());
            assertTrue(cookie.isHttpOnly());
            assertTrue(cookie.getSecure());
        }

}