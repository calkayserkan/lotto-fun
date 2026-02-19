package com.serkan.lottofun.ticketservice.repository;

import com.serkan.lottofun.ticketservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    public List<Ticket> findByUserId(Long userId);
    public List<Ticket> findByDrawId(Long drawId);
    long countByUserId(Long userId);
    boolean existsByDrawIdAndSelectedNumbers(
            Long drawId,
            String selectedNumbers
    );
}
