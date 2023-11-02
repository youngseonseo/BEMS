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
public class BuildingPerTenMinute {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "buildingPerTenMinute_id")
    public Long id;

    @Column
    public Timestamp timestamp;

    @Column
    public Integer building;

    @Column
    public Integer floor;

    @Column
    public Integer consumption;

    public BuildingPerTenMinute(Timestamp timestamp, Integer building, Integer floor, Integer consumption) {
        this.timestamp = timestamp;
        this.building = building;
        this.floor = floor;
        this.consumption = consumption;
    }
}
