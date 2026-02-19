package com.serkan.lottofun.drawservice.controller;

import com.serkan.lottofun.drawservice.dto.DtoDrawResponse;
import com.serkan.lottofun.drawservice.dto.RestPageableRequest;
import com.serkan.lottofun.drawservice.entity.Draw;
import com.serkan.lottofun.drawservice.repository.DrawRepository;
import com.serkan.lottofun.drawservice.service.IDrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/draws")
@RequiredArgsConstructor
public class DrawController {
    private final IDrawService drawService;
    private final DrawRepository drawRepository;

    @GetMapping("/list")
    public List<DtoDrawResponse> getAllDraws() {
        return drawService.getAllDraws();
    }
    @GetMapping("/open-list")
    public List<DtoDrawResponse> getAllOpenDraws() {
        return drawService.getAllOpenDraws();
    }
    @GetMapping("/{id}")
    public DtoDrawResponse getDrawById(@PathVariable Long id) {
        return drawService.getDrawById(id);
    }
    @GetMapping("/list/pageable")
    public ResponseEntity<Page<DtoDrawResponse>> findAllPegeable( @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Draw> draws = drawService.findAllPegeable(pageable);

        Page<DtoDrawResponse> response = draws.map(draw -> {
            DtoDrawResponse dto = new DtoDrawResponse();
            BeanUtils.copyProperties(draw, dto);
            return dto;
        });

        return ResponseEntity.ok(response);
    }
    @GetMapping("/draw-info/{id}")
    public ResponseEntity<DtoDrawResponse> getDraw(@PathVariable Long id) {
        Draw draw = drawRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Draw not found"));

        DtoDrawResponse response = new DtoDrawResponse();
        BeanUtils.copyProperties(draw, response);
        return ResponseEntity.ok(response);
    }
}
