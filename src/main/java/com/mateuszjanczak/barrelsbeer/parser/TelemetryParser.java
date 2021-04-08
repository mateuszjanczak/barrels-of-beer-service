package com.mateuszjanczak.barrelsbeer.parser;

import org.springframework.stereotype.Component;

@Component
public class TelemetryParser {
    public TelemetryData parseRawData(String value) {
        String data = value.trim().replace(" ", "");

        String binaryFlowCounter = data.substring(0, 30);
        String binaryBarrelTemperature = data.substring(46, 60);

        float flowCounter = Float.intBitsToFloat(Integer.parseInt(binaryFlowCounter, 2)) * 1000;
        float barrelTemperature = Integer.parseInt(binaryBarrelTemperature, 2) / 10.0f;

        TelemetryData telemetryData = new TelemetryData();
        telemetryData.setFlowCounter(flowCounter);
        telemetryData.setBarrelTemperature(barrelTemperature);

        return telemetryData;
    }
}
