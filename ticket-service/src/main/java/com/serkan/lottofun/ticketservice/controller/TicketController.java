package com.serkan.lottofun.ticketservice.controller;

import com.serkan.lottofun.ticketservice.dto.DtoTicketRequest;
import com.serkan.lottofun.ticketservice.dto.DtoTicketResponse;
import com.serkan.lottofun.ticketservice.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final ITicketService ticketService;

    @PostMapping("/purchase")
    public ResponseEntity<DtoTicketResponse> purchaseTicket(@RequestBody DtoTicketRequest request, Authentication authentication) {
        String username = authentication.getName();
        DtoTicketResponse response =
                ticketService.purchaseTicket(
                        username,
                        request.getDrawId(),
                        request.getSelectedNumbers()
                );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/user/{userId}")
    public List<DtoTicketResponse> getTicketsByUserId(@PathVariable  Long userId) {

        return ticketService.getTicketsByUserId(userId);
    }
    @GetMapping("/draw/{drawId}")
    public List<DtoTicketResponse> getTicketsByDrawId(@PathVariable Long drawId) {

        return ticketService.getTicketsByDrawId(drawId);
    }
}
