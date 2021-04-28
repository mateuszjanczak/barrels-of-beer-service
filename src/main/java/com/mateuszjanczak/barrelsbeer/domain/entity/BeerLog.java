package com.mateuszjanczak.barrelsbeer.domain.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Document
@ToString
public class BeerLog {
    @Id
    String id;

    String barrelContent;

    List<String> barrelTapLogs;

    long amount;

    Date startDate;

    Date endDate;
}
