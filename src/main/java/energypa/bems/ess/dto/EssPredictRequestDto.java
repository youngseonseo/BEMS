package energypa.bems.ess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class EssPredictRequestDto {


    @JsonProperty("TIMESTAMP")
    private Timestamp timestamp;

    @JsonProperty("SOC")
    private Double soc;

    @JsonProperty("BATTERY_POWER")
    private Double batteryPower;

    @JsonProperty("561_CONSUMPTION(kW)")
    private Double AConsumption;

    @JsonProperty("562_CONSUMPTION(kW)")
    private Double BConsumption;

    @JsonProperty("563_CONSUMPTION(kW)")
    private Double CConsumption;

}
