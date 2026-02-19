package com.serkan.lottofun.ticketservice.dto;

import com.serkan.lottofun.common.enums.DrawStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class DrawResponse {
    private Long id;
    private LocalDateTime drawDate;
    private BigDecimal ticketPrice;
    private BigDecimal jackpotPool;
    private DrawStatus status;

}
