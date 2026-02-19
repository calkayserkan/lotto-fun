package com.serkan.lottofun.authservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private BigDecimal balance;
    private LocalDateTime createdAt;
}
