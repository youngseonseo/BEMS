package energypa.bems.ess.dto;

import lombok.Data;

@Data
public class BusDto{

    private Integer A_bus;
    private Integer B_bus;
    private Integer C_bus;

    public BusDto(Integer a_bus, Integer b_bus, Integer c_bus) {
        A_bus = a_bus;
        B_bus = b_bus;
        C_bus = c_bus;
    }
}
