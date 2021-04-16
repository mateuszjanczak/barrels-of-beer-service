package com.mateuszjanczak.barrelsbeer.driver;

import com.mateuszjanczak.barrelsbeer.domain.entity.BarrelTap;
import com.mateuszjanczak.barrelsbeer.service.BarrelTapService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Objects;

//@Component
public class SensorScheduler {

    private final BarrelTapService barrelTapService;
    private RestTemplate restTemplate;
    private List<BarrelTap> barrelTapList;

    public SensorScheduler(BarrelTapService barrelTapService) {
        this.barrelTapService = barrelTapService;
        init();
    }

    private void init() {
        barrelTapList = barrelTapService.getBarrelTapList();
        restTemplate = new RestTemplate();
    }

    @Scheduled(fixedRate = 1000)
    public void readData() {
        for(BarrelTap barrelTap: barrelTapList) {
            int barrelTapId = barrelTap.getBarrelTapId();
            String url = "http://192.168.136.110//iolinkmaster/port["+ barrelTapId +"]/iolinkdevice/pdin/getdata";
            ResponseEntity<SensorData> optionalSensorData = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, SensorData.class);
            if(optionalSensorData.getStatusCode().is2xxSuccessful()) {
                String hex = Objects.requireNonNull(optionalSensorData.getBody()).getData().getValue();
                barrelTapService.hitBarrelTap(barrelTapId, hex);
                System.out.println("TAP ID: " + barrelTapId + ", URL: " + url + ", HEX: " + hex);
            }
        }
        System.out.println("UPDATE DATE: " + new Date());
    }
}
