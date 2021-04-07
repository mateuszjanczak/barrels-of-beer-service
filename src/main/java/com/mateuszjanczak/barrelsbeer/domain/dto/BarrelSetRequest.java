package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BarrelSetRequest {
    @NotBlank
    @NotNull
    String barrelName;

    @NotBlank
    @NotNull
    String barrelContent;

    @Range(min = 1)
    @NotNull
    int capacity;
}
