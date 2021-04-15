package com.mateuszjanczak.barrelsbeer.domain.dto;

import com.mateuszjanczak.barrelsbeer.domain.enums.BarrelContentType;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarrelSetRequest {
    @NotBlank
    @NotNull
    String barrelName;

    @NotNull
    BarrelContentType barrelContent;

    @Range(min = 1)
    @NotNull
    int totalCapacity;
}
