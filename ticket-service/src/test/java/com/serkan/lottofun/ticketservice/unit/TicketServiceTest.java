package com.serkan.lottofun.ticketservice.unit;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.ticketservice.dto.DrawResponse;
import com.serkan.lottofun.ticketservice.dto.UserResponse;
import com.serkan.lottofun.ticketservice.entity.Ticket;
import com.serkan.lottofun.ticketservice.repository.TicketRepository;
import com.serkan.lottofun.ticketservice.service.TicketServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void testPurchaseTicket() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer fake-token");
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(request)
        );
        DrawResponse drawResponse = new DrawResponse();
        drawResponse.setId(1L);
        drawResponse.setStatus(DrawStatus.DRAW_OPEN);
        drawResponse.setTicketPrice(BigDecimal.TEN);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.GET),
                any(),
                eq(DrawResponse.class)
        )).thenReturn(ResponseEntity.ok(drawResponse));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(5L);
        userResponse.setUsername("serkan");

        when(restTemplate.exchange(
                contains("users"),
                eq(HttpMethod.GET),
                any(),
                eq(UserResponse.class)
        )).thenReturn(ResponseEntity.ok(userResponse));

        when(ticketRepository.existsByDrawIdAndSelectedNumbers(anyLong(), anyString()))
                .thenReturn(false);

        when(ticketRepository.save(any(Ticket.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ticketService.purchaseTicket("serkan", 1L, "1,2,3,4,5");

        verify(ticketRepository).save(any(Ticket.class));
    }
}
