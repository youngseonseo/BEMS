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
public class EssBattery {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "essBattery_id")
    public Long id;

    @Column
    public Timestamp timestamp;

    @Column
    public Integer A_bus;

    @Column
    public Integer B_bus;

    @Column
    public Integer C_bus;

    public EssBattery(Timestamp timestamp, Integer a_bus, Integer b_bus, Integer c_bus) {
        this.timestamp = timestamp;
        A_bus = a_bus;
        B_bus = b_bus;
        C_bus = c_bus;
    }
}
