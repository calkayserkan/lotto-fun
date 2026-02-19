package com.serkan.lottofun.ticketservice;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.ticketservice.dto.DrawResponse;
import com.serkan.lottofun.ticketservice.dto.DtoTicketResponse;
import com.serkan.lottofun.ticketservice.dto.UserResponse;
import com.serkan.lottofun.ticketservice.entity.Ticket;
import com.serkan.lottofun.ticketservice.repository.TicketRepository;
import com.serkan.lottofun.ticketservice.service.ITicketService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TicketServiceIntegrationTests {
    @Autowired
    private ITicketService ticketService;

    @Autowired
    private TicketRepository ticketRepository;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private RabbitTemplate rabbitTemplate;



    @BeforeEach
    void cleanDb() {
        ticketRepository.deleteAll();
    }

    @Test
    public void testPurchaseTicket() {
        String username = "serkan";
        Long drawId = 1L;
        String numbers = "1,2,3,4,5";

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        mockRequest.addHeader("Authorization", "Bearer fake-token");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
        DrawResponse drawResponse = new DrawResponse();
        drawResponse.setId(drawId);
        drawResponse.setStatus(DrawStatus.DRAW_OPEN);
        drawResponse.setTicketPrice(new BigDecimal("10"));

        when(restTemplate.exchange(
                contains("/draws/"),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(DrawResponse.class)
        )).thenReturn(new ResponseEntity<>(drawResponse, HttpStatus.OK));

        // prepare user-service response
        UserResponse userResponse = new UserResponse();
        userResponse.setId(11L);
        userResponse.setUsername(username);
        userResponse.setBalance(new BigDecimal("200"));
        when(restTemplate.exchange(
                contains("/users/"),
                any(HttpMethod.class),
                any(HttpEntity.class),
                eq(UserResponse.class)
        )).thenReturn(new ResponseEntity<>(userResponse, HttpStatus.OK));
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), any(Object.class));
        DtoTicketResponse response = ticketService.purchaseTicket(username, drawId, numbers);
        assertNotNull(response);
        assertEquals(drawId, response.getDrawId());
        assertEquals(numbers, response.getSelectedNumbers());

        List<Ticket> tickets = ticketRepository.findAll();
        assertEquals(1, tickets.size());
        assertEquals(numbers, tickets.get(0).getSelectedNumbers());
        assertEquals(userResponse.getId(), tickets.get(0).getUserId());
        RequestContextHolder.resetRequestAttributes();

    }
}
