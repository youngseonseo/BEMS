package energypa.bems.predictElec.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class RequestElecDto {

    private String timestamp;
    private Integer Building;
    private Integer Floor;
    private Integer Consumption;


}
