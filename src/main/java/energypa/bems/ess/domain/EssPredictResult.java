package energypa.bems.ess.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor public class EssPredictResult {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "essPredictResult_id")
    public Long id;

    @Column
    private Timestamp timestamp;

    @Column
    private Double soc;

    @Column
    private Double batteryPower;

    @Column
    private Double predictedConsumption;

    @Column
    private Double touRate;

    @Column
    private Double threshold;

    @Column
    private Double netLoad;

    public EssPredictResult(Timestamp timestamp, Double soc, Double batteryPower, Double predictedConsumption, Double touRate, Double threshold, Double netLoad) {
        this.timestamp = timestamp;
        this.soc = soc;
        this.batteryPower = batteryPower;
        this.predictedConsumption = predictedConsumption;
        this.touRate = touRate;
        this.threshold = threshold;
        this.netLoad = netLoad;
    }
}
