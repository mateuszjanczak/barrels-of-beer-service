package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarrelAddRequest {
    @NotBlank
    @NotNull
    String barrelName;

    @Range(min = 1)
    @NotNull
    int totalCapacity;
}
