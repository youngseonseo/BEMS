package energypa.bems.energy.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Schema(description = "층 별 에너지 사용량 모니터링 info")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingPerTenMinute {

    @Schema(description = "데이터 번호", example = "1")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "buildingPerTenMinute_id")
    public Long id;

    @Schema(description = "시간", example = "2023-03-15T21:30:00.000+00:00")
    @Column
    public Timestamp timestamp;

    @Schema(description = "동", example = "561")
    @Column
    public Integer building;

    @Schema(description = "층", example = "1")
    @Column
    public Integer floor;

    @Schema(description = "전력 사용량", example = "225")
    @Column
    public Integer consumption;

    public BuildingPerTenMinute(Timestamp timestamp, Integer building, Integer floor, Integer consumption) {
        this.timestamp = timestamp;
        this.building = building;
        this.floor = floor;
        this.consumption = consumption;
    }
}
