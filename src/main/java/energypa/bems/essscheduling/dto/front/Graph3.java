package energypa.bems.essscheduling.dto.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "properties in graph3")
@Data
@Builder
public class Graph3 {

    @Schema(description = "timestamp (per minute)", example = "2022-08-27 12:02:00")
    private String timestamp;

    @Schema(description = "soc", example = "49.99865")
    private Double soc;
}
