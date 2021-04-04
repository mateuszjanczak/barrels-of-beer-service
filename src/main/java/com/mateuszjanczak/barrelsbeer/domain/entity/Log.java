package com.mateuszjanczak.barrelsbeer.domain.entity;

import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Log {

    @Id
    String id;

    String barrelId;

    String barrelName;

    String beerType;

    int capacity;

    Date date;

    LogType logType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarrelId() {
        return barrelId;
    }

    public String getBarrelName() {
        return barrelName;
    }

    public void setBarrelName(String barrelName) {
        this.barrelName = barrelName;
    }

    public void setBarrelId(String barrelId) {
        this.barrelId = barrelId;
    }

    public String getBeerType() {
        return beerType;
    }

    public void setBeerType(String beerType) {
        this.beerType = beerType;
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
}
