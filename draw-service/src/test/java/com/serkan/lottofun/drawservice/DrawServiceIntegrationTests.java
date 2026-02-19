package com.serkan.lottofun.drawservice;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.drawservice.dto.DtoDrawResponse;
import com.serkan.lottofun.drawservice.entity.Draw;
import com.serkan.lottofun.drawservice.repository.DrawRepository;
import com.serkan.lottofun.drawservice.scheduled.ScheduledManagementStarter;
import com.serkan.lottofun.drawservice.service.IDrawService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@Transactional
@ActiveProfiles("test")
@SpringBootTest
public class DrawServiceIntegrationTests {

    @Autowired
    private DrawRepository drawRepository;

    @Autowired
    private ScheduledManagementStarter scheduler;
    @Autowired
    private IDrawService drawService;

    @BeforeEach
    void cleanDb() {
        drawRepository.deleteAll();
    }

    @Test
    void testCloseExpiredDraw() {
        Draw draw = new Draw();
        draw.setStatus(DrawStatus.DRAW_OPEN);
        draw.setDrawDate(LocalDateTime.now().minusMinutes(10));
        draw.setJackpotPool(BigDecimal.valueOf(1000));
        draw.setTicketPrice(BigDecimal.valueOf(10));
        draw.setCreatedAt(LocalDateTime.now());
        drawRepository.save(draw);
        scheduler.closeExpiredDraws();
        Draw updated = drawRepository.findById(draw.getId()).orElseThrow();
        assertEquals(DrawStatus.DRAW_CLOSED, updated.getStatus());
    }
    @Test
    void testGetDraw() {
        Draw draw = new Draw();
        draw.setCreatedAt(LocalDateTime.now());
        draw.setDrawDate(LocalDateTime.now().plusDays(1));
        draw.setStatus(DrawStatus.DRAW_OPEN);
        draw.setJackpotPool(BigDecimal.valueOf(1000));
        draw.setTicketPrice(BigDecimal.valueOf(10));

        drawRepository.save(draw);
        List<DtoDrawResponse> draws = drawService.getAllDraws();
        assertEquals(1, draws.size());
    }
}
