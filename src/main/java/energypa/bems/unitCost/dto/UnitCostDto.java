package energypa.bems.unitCost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitCostDto {

    private Integer year;
    private Integer month;
    private Integer metroCd;
    private Integer cityCd;

}
