package energypa.bems.monitoring.dto;

import energypa.bems.awsS3.domain.GalleryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class TotalConsumption {

    @Schema(description = "timestamp(per min)", example = "2022-07-18 00:00:00.000000")
    public Timestamp timestamp;

    @Schema(description = "total power consumption of 561, 562 and 563", example = "168")
    public Integer totalConsumption;

    @Schema(description = "total power consumption of 561, 562 and 563", example = "168")
    public Integer totalPredictConsumption;

    public TotalConsumption(Timestamp timestamp, Integer totalConsumption, Integer totalPredictConsumption) {
        this.timestamp = timestamp;
        this.totalConsumption = totalConsumption;
        this.totalPredictConsumption = totalPredictConsumption;
    }
}
