package energypa.bems.energyPattern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class EnergyConsumptionDto {
    private Integer totalConsumption;      // 전날 소비 총량
    private Double morningPercent;         // 오전 소비 퍼센트
    private Double afternoonPercent;       // 오후 소비 퍼센트
    private Double eveningPercent;         // 저녁 소비 퍼센트
    private Double lateNightPercent;       // 심야 소비 퍼센트
    private Integer mostConsumptionTimeStartTime;  // 가장 많은 소비 시간대 시작
    private Integer mostConsumptionTimeEndTime;  // 가장 많은 소비 시간대 끝
}
