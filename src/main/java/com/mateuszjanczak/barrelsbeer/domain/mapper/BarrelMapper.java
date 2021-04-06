package com.mateuszjanczak.barrelsbeer.domain.mapper;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.Barrel;
import org.springframework.stereotype.Component;

@Component
public class BarrelMapper {
    public Barrel dtoToEntity(BarrelAddRequest barrelAddRequest) {
        Barrel barrel = new Barrel();
        barrel.setId(barrelAddRequest.getId());
        barrel.setTotalCapacity(barrelAddRequest.getTotalCapacity());
        return barrel;
    }

    public BarrelHitResponse barrrelToHitResponse(Barrel barrel) {
        BarrelHitResponse barrelHitResponse = new BarrelHitResponse();
        barrelHitResponse.setBarrelId(barrel.getId());
        barrelHitResponse.setOldValue(barrel.getCapacity() + 1);
        barrelHitResponse.setNewValue(barrel.getCapacity());
        return barrelHitResponse;
    }
}
