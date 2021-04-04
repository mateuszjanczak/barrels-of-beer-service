package com.mateuszjanczak.barrelsbeer.domain.mapper;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import org.springframework.stereotype.Component;

@Component
public class BarrelMapper {
    public Barrel dtoToEntity(BarrelAddRequest barrelAddRequest) {
        Barrel barrel = new Barrel();
        barrel.setBarrelName(barrelAddRequest.getBarrelName());
        barrel.setTotalCapacity(barrelAddRequest.getTotalCapacity());
        return barrel;
    }
}
