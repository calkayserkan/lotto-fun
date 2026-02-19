package com.serkan.lottofun.drawservice.service;

import com.serkan.lottofun.common.enums.DrawStatus;
import com.serkan.lottofun.drawservice.dto.DtoDrawResponse;
import com.serkan.lottofun.drawservice.entity.Draw;
import com.serkan.lottofun.drawservice.exception.BaseException;
import com.serkan.lottofun.drawservice.exception.ErrorMessage;
import com.serkan.lottofun.drawservice.repository.DrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements IDrawService {
    private final DrawRepository drawRepository;

    @Override
    public List<DtoDrawResponse> getAllDraws() {
        List<DtoDrawResponse> dtoDrawList = new ArrayList<>();
        List<Draw> drawList = drawRepository.findAll();
        for (Draw draw : drawList) {
            DtoDrawResponse dtoDrawResponse = new DtoDrawResponse();
            BeanUtils.copyProperties(draw, dtoDrawResponse);
            dtoDrawList.add(dtoDrawResponse);
        }
        return dtoDrawList;
    }

    @Override
    public List<DtoDrawResponse> getAllOpenDraws() {
        List<DtoDrawResponse> dtoDrawList = new ArrayList<>();
        List<Draw> drawList = drawRepository.findByStatusAndDrawDateAfter(DrawStatus.DRAW_OPEN, LocalDateTime.now());
        for (Draw draw : drawList) {
            DtoDrawResponse dtoDrawResponse = new DtoDrawResponse();
            BeanUtils.copyProperties(draw, dtoDrawResponse);
            dtoDrawList.add(dtoDrawResponse);
        }
        return dtoDrawList;
    }

    @Override
    public DtoDrawResponse getDrawById(Long id) {
        DtoDrawResponse dtoDrawResponse = new DtoDrawResponse();
        Optional<Draw> optional = drawRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BaseException(ErrorMessage.DRAW_NOT_FOUND);
        }
        Draw draw = optional.get();
        BeanUtils.copyProperties(draw, dtoDrawResponse);
        return dtoDrawResponse;
    }

    @Override
    public Page<Draw> findAllPegeable(Pageable pageable) {
        return drawRepository.findAll(pageable);
    }
}
