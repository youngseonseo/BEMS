package energypa.bems.essscheduling.dto.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(description = "ESS battery scheduling monitoring")
@Data
@Builder
public class EssSchFrontResponseDto {

    @Schema(description = "graph1")
    private Graph1 graph1;

    @Schema(description = "graph2")
    private Graph2 graph2;

    @Schema(description = "graph3")
    private Graph3 graph3;
}
