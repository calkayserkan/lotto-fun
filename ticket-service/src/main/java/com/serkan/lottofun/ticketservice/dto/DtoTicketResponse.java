package com.serkan.lottofun.ticketservice.dto;

import com.serkan.lottofun.ticketservice.entity.PrizeType;
import com.serkan.lottofun.ticketservice.entity.TicketStatus;
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
public class DtoTicketResponse {
    private Long id;
    private Long drawId;
    private Long userId;
    private String selectedNumbers;
    private String ticketNumber;
    private LocalDateTime purchaseDate;
    private TicketStatus ticketStatus;
    private Integer matchCount;
    private PrizeType prizeType;
    private BigDecimal prizeAmount;
}
