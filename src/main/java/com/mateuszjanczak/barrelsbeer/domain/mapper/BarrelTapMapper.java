package com.mateuszjanczak.barrelsbeer.domain.mapper;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAdd;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHit;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import org.springframework.stereotype.Component;

@Component
public class BarrelTapMapper {
    public BarrelTap dtoToEntity(BarrelTapAdd barrelTapAdd) {
        BarrelTap barrelTap = new BarrelTap();
        barrelTap.setBarrelTapId(barrelTapAdd.getBarrelTapId());
        return barrelTap;
    }

    public BarrelTapHit barrelToHitResponse(BarrelTap barrelTap) {
        BarrelTapHit barrelTapHit = new BarrelTapHit();
        barrelTapHit.setBarrelTapId(barrelTap.getBarrelTapId());
        barrelTapHit.setNewCapacity(barrelTap.getCurrentLevel());
        barrelTapHit.setNewTemperature(barrelTap.getTemperature());
        return barrelTapHit;
    }
}
