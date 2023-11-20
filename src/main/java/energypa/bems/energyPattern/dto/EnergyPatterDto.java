package energypa.bems.energyPattern.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class EnergyPatterDto {
    private LocalDate standardTimestamp;
    private EnergyConsumptionDto energyConsumptionDto;
    private EnergySaveCostDto energySaveCostDto;
    private EnergyCompareDto energyCompareDto;
}
