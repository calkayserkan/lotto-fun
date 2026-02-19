package com.serkan.lottofun.authservice.event;
import com.serkan.lottofun.common.event.TicketPurchasedEvent;
import com.serkan.lottofun.authservice.config.RabbitMQConfig;
import com.serkan.lottofun.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketPurchasedListener {
    private final IUserService userService;

    @RabbitListener(queues = RabbitMQConfig.TICKET_PURCHASED_QUEUE)
    public void handleTicketPurchased(TicketPurchasedEvent event) {

        userService.decreaseBalance(
                event.getUserId(),
                event.getTicketPrice()
        );
    }
}
