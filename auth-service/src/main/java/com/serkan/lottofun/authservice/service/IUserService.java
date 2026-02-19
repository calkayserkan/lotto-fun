package com.serkan.lottofun.authservice.service;

import com.serkan.lottofun.authservice.dto.UserResponse;
import com.serkan.lottofun.authservice.entity.User;

import java.math.BigDecimal;

public interface IUserService {
    public UserResponse getUserById(Long id);

    public UserResponse getUserByUsername(String username);

    public void updateBalance(Long id, BigDecimal amount);

    public void decreaseBalance(Long userId, BigDecimal ticketPrice);

    public void increaseBalance(Long userId, BigDecimal prizeAmount);

}
