package energypa.bems.essscheduling.dto.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EssSchAiRequestDto {

    @JsonProperty("TIMESTAMP")
    private String timestamp;

    @JsonProperty("SOC")
    private Double soc;

    @JsonProperty("BATTERY_POWER")
    private Double batteryPower;

    @JsonProperty("561_CONSUMPTION(kW)")
    private Double consumptionOf561;

    @JsonProperty("562_CONSUMPTION(kW)")
    private Double consumptionOf562;

    @JsonProperty("563_CONSUMPTION(kW)")
    private Double consumptionOf563;
}
