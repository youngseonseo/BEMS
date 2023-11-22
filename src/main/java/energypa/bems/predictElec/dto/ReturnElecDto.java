package energypa.bems.predictElec.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor

public class ReturnElecDto {
    private Timestamp timestamp;

    private Double prediction;

    private Integer building;

    private Integer floor;

}
