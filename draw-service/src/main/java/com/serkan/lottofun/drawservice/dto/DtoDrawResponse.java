package com.serkan.lottofun.drawservice.dto;

import com.serkan.lottofun.common.enums.DrawStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoDrawResponse {
    private Long id;
    private DrawStatus status;
    private String winningNumbers;
    private LocalDateTime drawDate;
    private BigDecimal ticketPrice;
    private BigDecimal jackpotPool;
}
