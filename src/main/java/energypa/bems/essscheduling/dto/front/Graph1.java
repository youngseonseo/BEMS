package energypa.bems.essscheduling.dto.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "properties in graph1")
@Data
@Builder
public class Graph1 {

    @Schema(description = "timestamp (per minute)", example = "2022-08-27 12:02:00")
    private String timestamp;

    @Schema(description = "battery power",  example = "-0.05")
    private Double batteryPower;

    @Schema(description = "total power consumption of 561, 562 and 563",  example = "140")
    private Integer consumption;

    @Schema(description = "tou",  example = "132.2")
    private Double tou;
}
