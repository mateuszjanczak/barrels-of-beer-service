package com.mateuszjanczak.barrelsbeer.domain.entity;

import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Log {

    @Id
    String id;

    int barrelTapId;

    String barrelName;

    String barrelContent;

    int capacity;

    Date date;

    LogType logType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getBarrelTapId() {
        return barrelTapId;
    }

    public void setBarrelTapId(int barrelTapId) {
        this.barrelTapId = barrelTapId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getBarrelContent() {
        return barrelContent;
    }

    public void setBarrelContent(String barrelContent) {
        this.barrelContent = barrelContent;
    }
}
