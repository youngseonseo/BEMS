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
public class TotalOneHour {    // 전 체동 사용량 저장 (한 시간 단위)


    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "totalOneHour_id")
    public Long id;

    @Column
    public Timestamp timestamp;

    @Column
    public Double consumption;

    public TotalOneHour(Timestamp timestamp, Double consumption) {
        this.timestamp = timestamp;
        this.consumption = consumption;
    }
}
