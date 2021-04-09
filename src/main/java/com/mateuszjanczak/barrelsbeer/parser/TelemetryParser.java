package com.mateuszjanczak.barrelsbeer.parser;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class TelemetryParser {
    public TelemetryData parseRawData(String value) {

        String hexData = value.trim().replace(" ", "");
        String binaryData = hexToBin(hexData);

        String binaryFlowCounter = binaryData.substring(0, 30);
        String binaryBarrelTemperature = binaryData.substring(46, 60);

        float flowCounter = Float.intBitsToFloat(Integer.parseInt(binaryFlowCounter, 2)) * 1000;
        float barrelTemperature = Integer.parseInt(binaryBarrelTemperature, 2) / 10.0f;

        TelemetryData telemetryData = new TelemetryData();
        telemetryData.setFlowCounter(flowCounter);
        telemetryData.setBarrelTemperature(barrelTemperature);

        return telemetryData;
    }

    private String hexToBin(String s) {
        return new BigInteger(s, 16).toString(2);
    }
}
