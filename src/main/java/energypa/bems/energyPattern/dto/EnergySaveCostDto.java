package energypa.bems.energyPattern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EnergySaveCostDto {
    private Integer DaySaveCost;
    private Integer WeekSaveCost;
    private Integer MonthSaveCost;

}
