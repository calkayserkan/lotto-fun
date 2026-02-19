package com.serkan.lottofun.common.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketPurchasedEvent implements Serializable {
    private Long userId;
    private Long drawId;
    private BigDecimal ticketPrice;
}
