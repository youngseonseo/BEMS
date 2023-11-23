package energypa.bems.essscheduling.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EssSchResponseDto {

    @JsonProperty("TIMESTAMP")
    private String timestamp;

    @JsonProperty("SOC")
    private Double soc;

    @JsonProperty("BATTERY_POWER")
    private Double batteryPower;

    @JsonProperty("PREDICTED_CONSUMPTION")
    private Double predictedConsumption;

    @JsonProperty("TOU(원/kWh)")
    private Double tou;

    @JsonProperty("THRESHOLD")
    private Double threshold;

    @JsonProperty("NET_LOAD")
    private Double netLoad;

    public EssSchResponseDto(Double soc, Double batteryPower) {
        this.soc = soc;
        this.batteryPower = batteryPower;
    }
}
