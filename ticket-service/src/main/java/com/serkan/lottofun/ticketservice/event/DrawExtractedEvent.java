package com.serkan.lottofun.ticketservice.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrawExtractedEvent implements Serializable {
    private Long drawId;
    private String winningNumbers;
    private LocalDateTime drawDate;
}
