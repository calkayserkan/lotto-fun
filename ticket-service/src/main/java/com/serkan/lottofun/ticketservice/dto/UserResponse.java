package com.serkan.lottofun.ticketservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private BigDecimal balance;
}
