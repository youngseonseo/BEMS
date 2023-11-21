package energypa.bems.energyPattern.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EnergyCompareDto {
    private Integer BuildingAverage;        // 아파트 평균 사용량
    private Integer FloorAverage;           // 우리집 평균 사용량
    private Integer CompareAveragePercent;  // 아파트와 우리집 사용량 비교 퍼센트
}

