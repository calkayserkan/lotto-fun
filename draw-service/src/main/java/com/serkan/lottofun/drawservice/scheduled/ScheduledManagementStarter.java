package com.serkan.lottofun.drawservice.scheduled;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.drawservice.entity.Draw;
import com.serkan.lottofun.drawservice.event.DrawEventPublisher;
import com.serkan.lottofun.drawservice.repository.DrawRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor()
public class ScheduledManagementStarter {

    private final DrawRepository drawRepository;
    private final DrawEventPublisher drawEventPublisher;
    private final SecureRandom secureRandom = new SecureRandom();

    @Scheduled(fixedRate = 60000)
    public void createDraw() {

        LocalDateTime now = LocalDateTime.now();
        Draw draw = new Draw();
        draw.setCreatedAt(now);
        draw.setDrawDate(now.plusMinutes(2));
        draw.setStatus(DrawStatus.DRAW_OPEN);
        draw.setTicketPrice(BigDecimal.valueOf(10.00));
        draw.setJackpotPool(BigDecimal.valueOf(1000.00));
        drawRepository.save(draw);
    }
    @Transactional
    @Scheduled(fixedRate = 15000)
    public void closeExpiredDraws(){

        List<Draw> extractedDraws = drawRepository.findByStatus(DrawStatus.DRAW_EXTRACTED);
        for (Draw draw : extractedDraws) {
            draw.setStatus(DrawStatus.DRAW_FINALIZED);
            drawRepository.save(draw);
        }

        List<Draw> processingDraws = drawRepository.findByStatus(DrawStatus.DRAW_PROCESSING);
        for (Draw draw : processingDraws) {
            draw.setWinningNumbers(generateNumbers());
            draw.setStatus(DrawStatus.DRAW_EXTRACTED);
            drawRepository.save(draw);
            drawEventPublisher.publishDrawExtracted(draw);
        }

        List<Draw> closedDraws = drawRepository.findByStatus(DrawStatus.DRAW_CLOSED);
        for (Draw draw : closedDraws) {
            draw.setStatus(DrawStatus.DRAW_PROCESSING);
            drawRepository.save(draw);
        }

        List<Draw> draws = drawRepository.findByStatusAndDrawDateBefore(DrawStatus.DRAW_OPEN, LocalDateTime.now());
        for (Draw draw : draws) {
            draw.setStatus(DrawStatus.DRAW_CLOSED);
            drawRepository.save(draw);
        }
    }

    private String generateNumbers() {
        List<Integer> numbers = IntStream.rangeClosed(1, 49)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(numbers,secureRandom);

        return numbers.stream()
                .limit(5)
                .sorted()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

}
