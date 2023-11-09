package energypa.bems.monitoring.dto;

import energypa.bems.login.domain.Authority;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Builder
@Getter
public class FloorInfo {

    private Authority authority;
    private int building;
    private int floor;
}
