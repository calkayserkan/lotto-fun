package com.serkan.lottofun.drawservice.entity;


import com.serkan.lottofun.common.enums.DrawStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "draw")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Draw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DrawStatus status;
    @Column(nullable = true,length = 20)
    private String winningNumbers;
    @Column(nullable = false)
    private LocalDateTime drawDate;
    @Column(nullable = false)
    private BigDecimal ticketPrice ;
    @Column(nullable = false)
    private BigDecimal jackpotPool ;
    @Column(nullable = false)
    private LocalDateTime createdAt;
}