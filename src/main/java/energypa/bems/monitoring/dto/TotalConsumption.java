package energypa.bems.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public class TotalConsumption {

    @Schema(description = "timestamp(per min)", example = "2022-07-18 00:00:00.000000")
    public Timestamp timestamp;

    @Schema(description = "total power consumption of 561, 562 and 563", example = "168.29569999999998")
    public Double totalConsumption;
}
