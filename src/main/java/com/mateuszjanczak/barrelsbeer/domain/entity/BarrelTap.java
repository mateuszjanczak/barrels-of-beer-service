package com.mateuszjanczak.barrelsbeer.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class BarrelTap {
    @Id
    int barrelTapId;

    String barrelName = "brak nazwy";

    String barrelContent = "brak zawartości";

    long capacity = 0;

    long totalCapacity = 1;

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

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }
}