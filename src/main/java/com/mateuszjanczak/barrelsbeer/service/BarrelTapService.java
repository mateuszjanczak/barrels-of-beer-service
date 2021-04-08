package com.mateuszjanczak.barrelsbeer.service;

import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelSetRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapAddRequest;
import com.mateuszjanczak.barrelsbeer.domain.dto.BarrelTapHitResponse;
import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import com.mateuszjanczak.barrelsbeer.domain.mapper.BarrelTapMapper;
import com.mateuszjanczak.barrelsbeer.domain.repository.BarrelTapRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BarrelTapService {

    private final BarrelTapRepository barrelTapRepository;
    private final LogService logService;
    private final BarrelTapMapper barrelTapMapper;

    public BarrelTapService(BarrelTapRepository barrelTapRepository, LogService logService, BarrelTapMapper barrelTapMapper) {
        this.barrelTapRepository = barrelTapRepository;
        this.logService = logService;
        this.barrelTapMapper = barrelTapMapper;
    }

    public void addBeerTap(BarrelTapAddRequest barrelTapAddRequest) {
        BarrelTap barrelTap = barrelTapMapper.dtoToEntity(barrelTapAddRequest);
        barrelTapRepository.save(barrelTap);
        logService.saveLog(barrelTap, LogType.BARREL_TAP_NEW);
    }

    public List<BarrelTap> getBarrelTapList() {
        return barrelTapRepository.findAll();
    }

    public void setBarrelOnBeerTap(int id, BarrelSetRequest barrelSetRequest) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if(optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();
            if(barrelSetRequest.getCapacity() >= 0) {
                barrelTap.setBarrelContent(barrelSetRequest.getBarrelContent());
                barrelTap.setBarrelName(barrelSetRequest.getBarrelName());
                barrelTap.setCapacity(barrelSetRequest.getCapacity());
                barrelTap.setTotalCapacity(barrelSetRequest.getCapacity());
                barrelTapRepository.save(barrelTap);
                logService.saveLog(barrelTap, LogType.BARREL_TAP_SET);
            }
        }
    }

    public Optional<BarrelTapHitResponse> hitBarrelTap(int id, int value) {
        Optional<BarrelTap> optionalBarrel = barrelTapRepository.findById(id);

        if(optionalBarrel.isPresent()) {
            BarrelTap barrelTap = optionalBarrel.get();
            if(barrelTap.getCapacity() > 0) {
                barrelTap.setCapacity(barrelTap.getTotalCapacity() - value);
                barrelTapRepository.save(barrelTap);
                logService.saveLog(barrelTap, LogType.BARREL_TAP_READ);
                return Optional.ofNullable(barrelTapMapper.barrelToHitResponse(barrelTap));
            }
        }

        return Optional.empty();
    }

    public BarrelTap getBarrelTapById(int id) {
        Optional<BarrelTap> optionalBarrelTap = barrelTapRepository.findById(id);
        return optionalBarrelTap.orElse(null);
    }
}
