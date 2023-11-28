package energypa.bems.essscheduling.dto.front;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "ESS battery scheduling monitoring prev data format")
@Data
@Builder
public class EssSchFrontPrevResponseDto {

    @Schema(description = "graph1")
    private List<Graph1> graph1;

    @Schema(description = "graph2")
    private List<Graph2> graph2;

    @Schema(description = "graph3")
    private List<Graph3> graph3;
}
