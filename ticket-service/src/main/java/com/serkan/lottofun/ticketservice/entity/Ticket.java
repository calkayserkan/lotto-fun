package com.serkan.lottofun.ticketservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ticket",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"draw_id", "selected_numbers"})
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "selected_numbers", nullable = false)
    private String selectedNumbers;
    @Column(nullable = false, unique = true, name = "ticket_number")
    private String ticketNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;
    @Column
    private Integer matchCount;
    @Enumerated(EnumType.STRING)
    private PrizeType prizeType;
    @Column
    private BigDecimal prizeAmount;
    @Column(name = "draw_id", nullable = false)
    private Long drawId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "purchase_date", nullable = false)
    private LocalDateTime purchaseDate;
}
