package com.mateuszjanczak.barrelsbeer.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class SensorClient {

    private final RestTemplate restTemplate;
    Logger log = LoggerFactory.getLogger(SensorClient.class);
    @Value("${sensor.url}")
    private String SENSOR_URL;

    public SensorClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<SensorData> getSensorData(int id) {
        try {
            ResponseEntity<SensorData> sensorDataResponseEntity = restTemplate.exchange(SENSOR_URL.replace("$SENSOR_ID", String.valueOf(id)), HttpMethod.GET, HttpEntity.EMPTY, SensorData.class);
            return Optional.ofNullable(sensorDataResponseEntity.getBody());
        } catch (RestClientException e) {
            log.error("Failed to connect to the server - " + e.getMessage());
        }

        return Optional.empty();
    }
}
