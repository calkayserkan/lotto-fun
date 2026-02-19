package com.serkan.lottofun.authservice.integration;

import com.serkan.lottofun.authservice.dto.AuthLoginRequest;
import com.serkan.lottofun.authservice.dto.AuthResponse;
import com.serkan.lottofun.authservice.dto.RegisterRequest;
import com.serkan.lottofun.authservice.dto.RegisterResponse;
import com.serkan.lottofun.authservice.exception.BaseException;
import com.serkan.lottofun.authservice.service.IAuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthIntegrationTest {
    @Autowired
    private IAuthService authService;

    @Test
    void testRegisterUser() {

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testUser");
        request.setPassword("123456");
        request.setBalance(BigDecimal.valueOf(100.00));
        RegisterResponse response = authService.register(request);
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("testUser", response.getUsername());
    }
    @Test
    void testAuthenticateUser() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testUser");
        registerRequest.setPassword("123456");
        registerRequest.setBalance(BigDecimal.valueOf(100));

        authService.register(registerRequest);

        AuthLoginRequest loginRequest = new AuthLoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("123456");

        AuthResponse response = authService.authenticate(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getToken());
    }
    @Test
    void testWrongPassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("TestUser");
        registerRequest.setPassword("123456");
        registerRequest.setBalance(BigDecimal.valueOf(100));

        authService.register(registerRequest);

        AuthLoginRequest loginRequest = new AuthLoginRequest();
        loginRequest.setUsername("TestUser");
        loginRequest.setPassword("1234");

        assertThrows(BaseException.class, () -> {
            authService.authenticate(loginRequest);
        });
    }
}
