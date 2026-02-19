package com.serkan.lottofun.drawservice.unit;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.drawservice.entity.Draw;
import com.serkan.lottofun.drawservice.event.DrawEventPublisher;
import com.serkan.lottofun.drawservice.repository.DrawRepository;
import com.serkan.lottofun.drawservice.scheduled.ScheduledManagementStarter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DrawResultTest {
    @Mock
    DrawRepository drawRepository;
    @Mock
    private DrawEventPublisher drawEventPublisher;
    @InjectMocks
    private ScheduledManagementStarter scheduler;

    @Test
    public void testCalculateWinner(){
        Draw draw = new Draw();
        draw.setId(1L);
        draw.setStatus(DrawStatus.DRAW_EXTRACTED);
        draw.setDrawDate(LocalDateTime.now().minusMinutes(10));

        when(drawRepository.findByStatus(any())).thenReturn(List.of(draw));

        when(drawRepository.save(any(Draw.class))).thenAnswer(invocation -> invocation.getArgument(0));

        scheduler.closeExpiredDraws();
        assertEquals(DrawStatus.DRAW_PROCESSING, draw.getStatus());
        assertNotNull(draw.getWinningNumbers());

        verify(drawRepository, atLeastOnce()).save(any(Draw.class));
        verify(drawEventPublisher).publishDrawExtracted(draw);
    }
}
