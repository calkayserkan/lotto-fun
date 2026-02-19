package com.serkan.lottofun.ticketservice.service;

import com.serkan.lottofun.ticketservice.dto.DtoTicketResponse;

import java.math.BigDecimal;
import java.util.List;

public interface ITicketService {
    public DtoTicketResponse purchaseTicket(String username, Long drawId, String selectedNumbers);
    public List<DtoTicketResponse> getTicketsByUserId(Long userId);
    public List<DtoTicketResponse> getTicketsByDrawId(Long drawId);
    void processDrawExtracted(Long drawId, String winningNumbers, BigDecimal jackpotPool);
}
