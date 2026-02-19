package com.serkan.lottofun.drawservice.service;

import com.serkan.lottofun.drawservice.dto.DtoDrawResponse;
import com.serkan.lottofun.drawservice.entity.Draw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDrawService {
    public List<DtoDrawResponse> getAllDraws();
    public List<DtoDrawResponse> getAllOpenDraws();
    public DtoDrawResponse getDrawById(Long id);
    Page<Draw> findAllPegeable(Pageable pageable);
}
