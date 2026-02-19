package com.serkan.lottofun.drawservice.event;
import com.serkan.lottofun.common.event.DrawExtractedEvent;
import com.serkan.lottofun.drawservice.config.RabbitMQConfig;
import com.serkan.lottofun.drawservice.entity.Draw;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class DrawEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishDrawExtracted(Draw draw) {
        DrawExtractedEvent event = new DrawExtractedEvent(
                draw.getId(),
                draw.getWinningNumbers(),
                draw.getDrawDate(),
                draw.getJackpotPool(),
                draw.getTicketPrice()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.DRAW_EXTRACTED_QUEUE,
                event
        );
    }
}
