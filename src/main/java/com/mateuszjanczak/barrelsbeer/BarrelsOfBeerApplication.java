package com.mateuszjanczak.barrelsbeer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BarrelsOfBeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BarrelsOfBeerApplication.class, args);
    }
}
