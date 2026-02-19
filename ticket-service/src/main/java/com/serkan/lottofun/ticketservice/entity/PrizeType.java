package com.serkan.lottofun.ticketservice.entity;

import lombok.Getter;

@Getter
public enum PrizeType {
    JACKPOT(50.0),   // 5 eşleşme - havuzun %50'si
    HIGH(25.0),      // 4 eşleşme - havuzun %25'i
    MEDIUM(15.0),    // 3 eşleşme - havuzun %15'i
    LOW(10.0),       // 2 eşleşme - havuzun %10'u
    NONE(0.0);       // 2'den az  - ödül yok

    private final double percentage;

    PrizeType(double percentage) {
        this.percentage = percentage;
    }
}
