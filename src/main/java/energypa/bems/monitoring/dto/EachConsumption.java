package energypa.bems.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public class EachConsumption {

    @Schema(description = "yesterday or week before or month before", example = "2022-08-03")
    public LocalDate date;

    @Schema(description = "power consumption of 561", example = "87094.06345000003")
    public Double totalAConsumption;

    @Schema(description = "power consumption of 562", example = "85759.40585000004")
    public Double totalBConsumption;

    @Schema(description = "power consumption of 563", example = "48706.65279999998")
    public Double totalCConsumption;
}
