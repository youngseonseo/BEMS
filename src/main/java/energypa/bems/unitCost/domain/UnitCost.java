package energypa.bems.unitCost.domain;

import lombok.Data;

@Data
public class UnitCost {
    private String year;
    private String month;
    private String metro;
    private String city;
    private String cntr;
    private Double unitCost;

    public UnitCost(String year, String month, String metro, String city, String cntr, Double unitCost) {
        this.year = year;
        this.month = month;
        this.metro = metro;
        this.city = city;
        this.cntr = cntr;
        this.unitCost = unitCost;
    }
}
