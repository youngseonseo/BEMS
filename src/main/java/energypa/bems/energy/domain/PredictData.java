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
public class PredictData {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "oredictData_id")
    public Long id;

    @Column
    public Double prediction;

    @Column
    public Timestamp timestamp;

    @Column
    public Integer building;

    @Column
    public Integer floor;
}

