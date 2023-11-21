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


    public BuildingPerMinute(Timestamp timestamp, Double a_Consumption, Double b_Consumption, Double c_Consumption) {
        this.timestamp = timestamp;
        A_Consumption = a_Consumption;
        B_Consumption = b_Consumption;
        C_Consumption = c_Consumption;
    }
}
