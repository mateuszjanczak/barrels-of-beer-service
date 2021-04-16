package com.mateuszjanczak.barrelsbeer.domain.entity;

import com.mateuszjanczak.barrelsbeer.domain.enums.LogType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ToString
@Document
public class BarrelTapLog {

    @Id
    String id;

    int barrelTapId;

    String barrelName;

    String barrelContent;

    long capacity;

    long totalUsage;

    long singleUsage;

    Date date;

    LogType logType;
}
