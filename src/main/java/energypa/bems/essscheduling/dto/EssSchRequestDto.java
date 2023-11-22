package energypa.bems.essscheduling.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EssSchRequestDto {

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
