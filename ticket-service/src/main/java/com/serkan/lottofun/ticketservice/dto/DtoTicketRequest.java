package com.serkan.lottofun.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DtoTicketRequest {
//    private Long userId;
    private Long drawId;
    private String selectedNumbers;
}
