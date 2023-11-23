package energypa.bems.energy.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class FloorOneHour {          // 각 동의 실제 사용량 (한 시간 단위)

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "floorOneHour_id")
    public Long id;

    @Column
    public Timestamp timestamp;

    @Column
    public Integer building;

    @Column
    public Integer floor;

    @Column
    public Double consumption;

    public FloorOneHour(Timestamp timestamp, Integer building, Integer floor, Double consumption) {
        this.timestamp = timestamp;
        this.building = building;
        this.floor = floor;
        this.consumption = consumption;
    }
}
