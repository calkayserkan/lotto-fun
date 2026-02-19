package com.serkan.lottofun.authservice.service;

import com.serkan.lottofun.authservice.dto.AuthLoginRequest;
import com.serkan.lottofun.authservice.dto.AuthResponse;
import com.serkan.lottofun.authservice.dto.RegisterRequest;
import com.serkan.lottofun.authservice.dto.RegisterResponse;

public interface IAuthService {
    public RegisterResponse register(RegisterRequest registerRequest);
    public AuthResponse authenticate(AuthLoginRequest loginRequest);

}
