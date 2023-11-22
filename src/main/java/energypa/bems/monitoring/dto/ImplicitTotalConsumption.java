package energypa.bems.monitoring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
public class ImplicitTotalConsumption {

    @Schema(description = "timestamp(per min)", example = "2022-07-18 00:00:00.000000")
    public Timestamp timestamp;

    @Schema(description = "total power consumption of 561, 562 and 563", example = "168")
    public Integer totalConsumption;

}
