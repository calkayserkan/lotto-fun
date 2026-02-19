package com.serkan.lottofun.drawservice.repository;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.drawservice.entity.Draw;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DrawRepository extends JpaRepository<Draw, Long> {
    Optional<Draw> findTopByOrderByCreatedAtDesc();
    //race condition ı engellemek için
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    List<Draw> findByStatusAndDrawDateBefore(DrawStatus status, LocalDateTime now);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "0")})
    List<Draw> findByStatus(DrawStatus status);
    List<Draw> findByStatusAndDrawDateAfter(DrawStatus status, LocalDateTime now);
    @Query(value = "from Draw")
    Page<Draw> findAllPegeable(Pageable pageable);
}
