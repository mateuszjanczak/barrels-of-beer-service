package com.mateuszjanczak.barrelsbeer.domain.mapper;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import org.springframework.stereotype.Component;

@Component
public class BarrelMapper {
    public BarrelTap dtoToEntity(BarrelTapAddRequest barrelTapAddRequest) {
        BarrelTap barrelTap = new BarrelTap();
        barrelTap.setBarrelTapId(barrelTapAddRequest.getBarrelTapId());
        return barrelTap;
    }

    public BarrelTapHitResponse barrelToHitResponse(BarrelTap barrelTap) {
        BarrelTapHitResponse barrelTapHitResponse = new BarrelTapHitResponse();
        barrelTapHitResponse.setBarrelTapId(barrelTap.getBarrelTapId());
        barrelTapHitResponse.setOldValue(barrelTap.getCapacity() + 1);
        barrelTapHitResponse.setNewValue(barrelTap.getCapacity());
        return barrelTapHitResponse;
    }
}
