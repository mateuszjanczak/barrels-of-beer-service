package com.mateuszjanczak.barrelsbeer.infrastructure;

import com.mateuszjanczak.barrelsbeer.domain.model.TelemetryData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SensorUtils {

    private final Map<String, String> digitsMap;

    {
        digitsMap = new HashMap<>();
        digitsMap.put("0", "0000");
        digitsMap.put("1", "0001");
        digitsMap.put("2", "0010");
        digitsMap.put("3", "0011");
        digitsMap.put("4", "0100");
        digitsMap.put("5", "0101");
        digitsMap.put("6", "0110");
        digitsMap.put("7", "0111");
        digitsMap.put("8", "1000");
        digitsMap.put("9", "1001");
        digitsMap.put("A", "1010");
        digitsMap.put("B", "1011");
        digitsMap.put("C", "1100");
        digitsMap.put("D", "1101");
        digitsMap.put("E", "1110");
        digitsMap.put("F", "1111");
    }

    public TelemetryData getTelemetryData(String value) {

        String hexData = value.trim().replace(" ", "");
        String binaryData = hexToBin(hexData);

        String binaryBarrelCurrentLevel = binaryData.substring(0, 32);
        String binaryBarrelTemperature = binaryData.substring(48, 62);

        float barrelCurrentLevel = Float.intBitsToFloat(Integer.parseInt(binaryBarrelCurrentLevel, 2)) * 1000;
        float barrelTemperature = Integer.parseInt(binaryBarrelTemperature, 2) / 10.0f;

        TelemetryData telemetryData = new TelemetryData();
        telemetryData.setCurrentLevel((long) barrelCurrentLevel);
        telemetryData.setTemperature(barrelTemperature);

        return telemetryData;
    }

    private String hexToBin(String s) {
        char[] hex = s.toCharArray();
        StringBuilder binaryString = new StringBuilder();
        for (char h : hex) {
            binaryString.append(digitsMap.get(String.valueOf(h)));
        }
        return binaryString.toString();
    }
}
