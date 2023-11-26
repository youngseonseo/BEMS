package energypa.bems.ess.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateConsumptionDto {

    private Double AConsumptionRate;
    private Double BConsumptionRate;
    private Double CConsumptionRate;
}
