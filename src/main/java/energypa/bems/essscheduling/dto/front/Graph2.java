package energypa.bems.essscheduling.dto.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "properties in graph2")
@Data
@Builder
public class Graph2 {

    @Schema(description = "timestamp (per minute)", example = "2022-08-27 12:02:00")
    private String timestamp;

    @Schema(description = "total power consumption of 561, 562 and 563",  example = "140")
    private Integer consumption;

    @Schema(description = "net load", example = "2.318482520222584")
    private Double netLoad;

    @Schema(description = "threshold", example = "2.7")
    private Double threshold;
}
