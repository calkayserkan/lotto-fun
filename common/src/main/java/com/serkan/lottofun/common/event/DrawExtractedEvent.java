package com.serkan.lottofun.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawExtractedEvent implements Serializable {
    private Long drawId;
    private String winningNumbers;
    private LocalDateTime drawDate;
    private BigDecimal jackpotPool;
    private BigDecimal ticketPrice;
}
