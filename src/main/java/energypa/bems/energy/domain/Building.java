package energypa.bems.energy.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

// 동별 에너지 소비 전력 도메인

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Building {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "building_id")
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
    public Double A_bus;

    @Column
    public Double B_bus;

    @Column
    public Double C_bus;

    public Building(Timestamp timestamp, Double a_Consumption, Double b_Consumption, Double c_Consumption, Double a_bus, Double b_bus, Double c_bus) {
        this.timestamp = timestamp;
        A_Consumption = a_Consumption;
        B_Consumption = b_Consumption;
        C_Consumption = c_Consumption;
        A_bus = a_bus;
        B_bus = b_bus;
        C_bus = c_bus;
    }
}
