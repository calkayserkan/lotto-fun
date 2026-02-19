package com.serkan.lottofun.authservice.controller;

import com.serkan.lottofun.authservice.dto.AuthLoginRequest;
import com.serkan.lottofun.authservice.dto.AuthResponse;
import com.serkan.lottofun.authservice.dto.RegisterRequest;
import com.serkan.lottofun.authservice.dto.RegisterResponse;
import com.serkan.lottofun.authservice.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthLoginRequest authLoginRequest) {
        return ResponseEntity.ok().body(authService.authenticate(authLoginRequest));
    }
}
