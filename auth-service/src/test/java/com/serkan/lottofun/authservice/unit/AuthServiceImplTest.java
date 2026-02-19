package com.serkan.lottofun.authservice.unit;

import com.serkan.lottofun.authservice.dto.RegisterRequest;
import com.serkan.lottofun.authservice.dto.RegisterResponse;
import com.serkan.lottofun.authservice.jwt.JWTService;
import com.serkan.lottofun.authservice.repository.UserRepository;
import com.serkan.lottofun.authservice.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testRegisterUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("serkan");
        request.setPassword("1234");
        request.setBalance(BigDecimal.valueOf(100));

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPass");

        when(userRepository.save(Mockito.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        verify(userRepository, times(1)).save(Mockito.any());
    }

}
