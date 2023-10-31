package energypa.bems.energy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingEnergyPrice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "buildingEnergyPrice_id")
    public Long id;

    @Column
    public Integer month;

    @Column
    public Integer Building;
    @Column
    public Integer floor;

    @Column
    public Integer totalConsumption;


    public BuildingEnergyPrice(Integer month, Integer building, Integer floor, Integer totalConsumption) {
        this.month = month;
        Building = building;
        this.floor = floor;
        this.totalConsumption = totalConsumption;
    }
}
