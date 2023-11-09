package energypa.bems.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(description = "동 별 모니터링 요청에 대한 response")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class MonitorBuildingResponse {

    @Schema(description = "graph1 - total power consumption of 561, 562 and 563")
    private List<TotalConsumption> graph1;

    @Schema(description = "graph2 - power usage yesterday or last week or last month in each apartment")
    private EachConsumption graph2;

    @Schema(description = "graph3 - power consumption for each apartment by hour or week or month")
    private List<EachConsumption> graph3;
}
