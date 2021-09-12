package com.mateuszjanczak.barrelsbeer.common;

public class BarrelTapHitException extends RuntimeException {
    public BarrelTapHitException() {
        super("The tap does not exist or the barrel capacity is 0");
    }
}
