package com.serkan.lottofun.ticketservice.event;
import com.serkan.lottofun.common.event.DrawExtractedEvent;
import com.serkan.lottofun.ticketservice.config.RabbitMQConfig;
import com.serkan.lottofun.ticketservice.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DrawExtractedListener {
    private final ITicketService ticketService;

//    @RabbitListener(queues = "draw.extracted.queue",
//            containerFactory = "rabbitListenerContainerFactory")
//    public void handle(DrawExtractedEvent event) {
//        ticketService.processDrawExtracted(
//                event.getDrawId(),
//                event.getWinningNumbers()
//        );
//    }
@RabbitListener(queues = RabbitMQConfig.DRAW_EXTRACTED_QUEUE,
        containerFactory = "rabbitListenerContainerFactory")
public void handle(DrawExtractedEvent event) {
    System.out.println("EVENT GELDİ: " + event.getDrawId());
    ticketService.processDrawExtracted(
            event.getDrawId(),
            event.getWinningNumbers(),
            event.getJackpotPool()
    );
}
}
