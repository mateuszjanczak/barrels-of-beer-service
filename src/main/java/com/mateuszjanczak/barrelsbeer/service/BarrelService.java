package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelMapper;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrelService {

    private final BarrelRepository barrelRepository;
    private final LogService logService;
    private final BarrelMapper barrelMapper;

    public BarrelService(BarrelRepository barrelRepository, LogService logService, BarrelMapper barrelMapper) {
        this.barrelRepository = barrelRepository;
        this.logService = logService;
        this.barrelMapper = barrelMapper;
    }

    public void addBarrel(BarrelAddRequest barrelAddRequest) {
        Barrel barrel = barrelMapper.dtoToEntity(barrelAddRequest);
        barrelRepository.save(barrel);
    }

    public List<Barrel> getBarrelList() {
        return barrelRepository.findAll();
    }

    public void setBarrel(String id, BarrelSetRequest barrelSetRequest) {
        Optional<Barrel> optionalBarrel = barrelRepository.findById(id);

        if(optionalBarrel.isPresent()) {
            Barrel barrel = optionalBarrel.get();
            barrel.setBeerType(barrelSetRequest.getBeerType());
            barrel.setCapacity(barrelSetRequest.getCapacity());
            barrelRepository.save(barrel);
            logService.saveLog(barrel, LogType.BEER_SET);
        }
    }

    public void hit(String id) {
        Optional<Barrel> optionalBarrel = barrelRepository.findById(id);

        if(optionalBarrel.isPresent()) {
            Barrel barrel = optionalBarrel.get();
            barrel.setCapacity(barrel.getCapacity() - 1);
            barrelRepository.save(barrel);
            logService.saveLog(barrel, LogType.BEER_HIT);
        }
    }
}
