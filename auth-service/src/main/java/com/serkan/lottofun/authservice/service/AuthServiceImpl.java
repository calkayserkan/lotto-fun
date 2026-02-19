package com.serkan.lottofun.authservice.service;

import com.serkan.lottofun.authservice.dto.AuthLoginRequest;
import com.serkan.lottofun.authservice.dto.AuthResponse;
import com.serkan.lottofun.authservice.dto.RegisterRequest;
import com.serkan.lottofun.authservice.dto.RegisterResponse;
import com.serkan.lottofun.authservice.entity.User;
import com.serkan.lottofun.authservice.exception.BaseException;
import com.serkan.lottofun.authservice.exception.ErrorMessage;
import com.serkan.lottofun.authservice.jwt.JWTService;
import com.serkan.lottofun.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  IAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationProvider authenticationProvider;
    private final JWTService jwtService;

    @Override
    public AuthResponse authenticate(AuthLoginRequest loginRequest) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            authenticationProvider.authenticate(auth);
            User user = userRepository
                    .findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BaseException(ErrorMessage.USER_NOT_FOUND));
            String token = jwtService.generateToken(user);
            return new AuthResponse(token);
        }catch (Exception e){
            throw new BaseException(ErrorMessage.INVALID_CREDENTIALS,e.getMessage());
        }
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername((registerRequest.getUsername()))) {
            throw new BaseException(ErrorMessage.USERNAME_ALREADY_EXIST);
        }
        RegisterResponse registerResponse = new RegisterResponse();
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setBalance(registerRequest.getBalance());
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        BeanUtils.copyProperties(savedUser,registerResponse);

        return registerResponse;
    }
}
