package energypa.bems.essscheduling.service;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.essscheduling.dto.front.EssSchFrontResponseDto;
import energypa.bems.essscheduling.dto.front.Graph1;
import energypa.bems.essscheduling.dto.front.Graph2;
import energypa.bems.essscheduling.dto.front.Graph3;
import energypa.bems.essscheduling.thread.EssSchThread;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EssSchService {

    private final BuildingPerMinuteRepository buildingRepository;
    private final EssPredictResultRepository essRepository;

    public List<EssSchFrontResponseDto> getEssSchPrevData() {

        List<BuildingPerMinute> bcPrevDataList = buildingRepository.getBuildingConsumptionPrevData(EssSchThread.buildingPerMinuteId);// offset 10에서 시작해서 500개 가져온다.
        List<EssPredictResult> essPrevDataList = essRepository.getEssSchPrevData(EssSchThread.buildingPerMinuteId); // offset 10에서 시작해서 500개 가져온다.
        EssSchThread.buildingPerMinuteId += 500;

        List<EssSchFrontResponseDto> essFrontPrevDataList = new ArrayList<>();

        for(int i=0; i<500; i++) {

            BuildingPerMinute buildingConsumption = bcPrevDataList.get(i);
            EssPredictResult essScheduling = essPrevDataList.get(i);

            EssSchFrontResponseDto essFrontPrevData = EssSchFrontResponseDto.builder()
                    .graph1(getGraph1(buildingConsumption, essScheduling))
                    .graph2(getGraph2(buildingConsumption, essScheduling))
                    .graph3(getGraph3(buildingConsumption, essScheduling))
                    .build();

            essFrontPrevDataList.add(essFrontPrevData);
        }

        return essFrontPrevDataList;
    }

    private Graph1 getGraph1(BuildingPerMinute buildingConsumption, EssPredictResult essScheduling) {

        return Graph1.builder()
                .timestamp(getTimestamp(essScheduling))
                .batteryPower(essScheduling.getBatteryPower())
                .consumption(sumConsumption(buildingConsumption))
                .tou(essScheduling.getTouRate())
                .build();
    }

    private Graph2 getGraph2(BuildingPerMinute buildingConsumption, EssPredictResult essScheduling) {

        return Graph2.builder()
                .timestamp(getTimestamp(essScheduling))
                .consumption(sumConsumption(buildingConsumption))
                .netLoad(essScheduling.getNetLoad())
                .threshold(essScheduling.getThreshold())
                .build();
    }

    private Graph3 getGraph3(BuildingPerMinute buildingConsumption, EssPredictResult essScheduling) {

        return Graph3.builder()
                .timestamp(getTimestamp(essScheduling))
                .soc(essScheduling.getSoc())
                .build();
    }

    private String getTimestamp(EssPredictResult essScheduling) {
        return essScheduling.getTimestamp().toString();
    }

    private int sumConsumption(BuildingPerMinute bdConsumption) {
        return (int) (bdConsumption.getA_Consumption() + bdConsumption.getB_Consumption() + bdConsumption.getC_Consumption());
    }
}
