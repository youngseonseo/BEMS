package energypa.bems.energy.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingPerMinute {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "buildingPerMinute_id")
    public Long id;

    @Column
    public Timestamp timestamp;

    @Column
    public Double A_Consumption;

    @Column
    public Double B_Consumption;

    @Column
    public Double C_Consumption;

    @Column
    public Integer A_bus;

    @Column
    public Integer B_bus;

    @Column
    public Integer C_bus;

    public BuildingPerMinute(Timestamp timestamp, Double a_Consumption, Double b_Consumption, Double c_Consumption, Integer a_bus, Integer b_bus, Integer c_bus) {
        this.timestamp = timestamp;
        A_Consumption = a_Consumption;
        B_Consumption = b_Consumption;
        C_Consumption = c_Consumption;
        A_bus = a_bus;
        B_bus = b_bus;
        C_bus = c_bus;
    }
}
