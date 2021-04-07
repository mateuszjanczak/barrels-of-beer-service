package com.mateuszjanczak.barrelsbeer.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BarrelTap {
    @Id
    int barrelTapId;

    String barrelName = "brak nazwy";

    String barrelContent = "brak zawartości";

    int capacity = 0;

    int totalCapacity = 1;

    public int getBarrelTapId() {
        return barrelTapId;
    }

    public void setBarrelTapId(int barrelTapId) {
        this.barrelTapId = barrelTapId;
    }

    public String getBarrelContent() {
        return barrelContent;
    }

    public void setBarrelContent(String barrelContent) {
        this.barrelContent = barrelContent;
    }

    public String getBarrelName() {
        return barrelName;
    }

    public void setBarrelName(String barrelName) {
        this.barrelName = barrelName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(int totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
}