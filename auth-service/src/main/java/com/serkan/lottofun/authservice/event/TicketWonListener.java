package com.serkan.lottofun.authservice.event;

import com.serkan.lottofun.authservice.config.RabbitMQConfig;
import com.serkan.lottofun.authservice.service.IUserService;
import com.serkan.lottofun.common.event.TicketWonEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketWonListener {
    private final IUserService userService;

    @RabbitListener(queues = RabbitMQConfig.TICKET_WON_QUEUE)
    public void handleTicketWon(TicketWonEvent event) {

        userService.increaseBalance(
                event.getUserId(),
                event.getPrizeAmount()
        );
    }
}
