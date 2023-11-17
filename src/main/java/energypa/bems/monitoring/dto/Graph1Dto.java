package energypa.bems.monitoring.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class Graph1Dto {

    public Timestamp timestamp;
    public Double totalConsumption;

    public Graph1Dto(Timestamp timestamp, Double totalConsumption) {
        this.timestamp = timestamp;
        this.totalConsumption = totalConsumption;
    }
}
