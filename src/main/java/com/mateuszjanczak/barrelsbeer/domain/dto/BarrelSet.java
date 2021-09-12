package com.mateuszjanczak.barrelsbeer.domain.dto;

import com.mateuszjanczak.barrelsbeer.domain.enums.ContentType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarrelSet {
    @NotBlank
    @NotNull
    String barrelName;

    @NotNull
    ContentType barrelContent;

    @Range(min = 1)
    @NotNull
    int capacity;
}
