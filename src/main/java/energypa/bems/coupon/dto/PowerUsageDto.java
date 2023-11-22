package energypa.bems.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PowerUsageDto {

    private Integer building;
    private Integer floor;
    private Integer totalConsumption;
}
