package com.mateuszjanczak.barrelsbeer.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class BarrelTapAdd {
    @Range(min = 1)
    @NotNull
    int barrelTapId;
}
