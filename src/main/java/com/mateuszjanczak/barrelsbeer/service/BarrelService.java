package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelMapper;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarrelService {

    private final BarrelRepository barrelRepository;
    private final BarrelMapper barrelMapper;

    public BarrelService(BarrelRepository barrelRepository, BarrelMapper barrelMapper) {
        this.barrelRepository = barrelRepository;
        this.barrelMapper = barrelMapper;
    }

    public void addBarrel(BarrelAddRequest barrelAddRequest) {
        Barrel barrel = barrelMapper.dtoToEntity(barrelAddRequest);
        barrelRepository.save(barrel);
    }

    public List<Barrel> getBarrelList() {
        return barrelRepository.findAll();
    }
}
