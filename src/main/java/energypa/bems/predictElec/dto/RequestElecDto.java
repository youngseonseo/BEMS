package energypa.bems.predictElec.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class RequestElecDto {

    @JsonProperty("TIMESTAMP")
    private String timestamp;

    @JsonProperty("BUILDING")
    private Integer Building;

    @JsonProperty("FLOOR")
    private Integer Floor;

    @JsonProperty("CONSUMPTION(kW)")
    private Double Consumption;


}
