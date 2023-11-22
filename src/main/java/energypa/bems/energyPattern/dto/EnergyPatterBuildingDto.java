package energypa.bems.energyPattern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@Builder
public class EnergyPatterBuildingDto {
    private LocalDate standardTimestamp;
    private EnergyConsumptionDto energyConsumptionDto;
    private EnergySaveCostDto energySaveCostDto1;
    private EnergySaveCostDto energySaveCostDto2;
    private EnergySaveCostDto energySaveCostDto3;
}
