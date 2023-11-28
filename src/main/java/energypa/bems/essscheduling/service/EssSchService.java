package energypa.bems.essscheduling.service;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.essscheduling.dto.front.*;
import energypa.bems.essscheduling.thread.EssSchThread;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.Graph;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EssSchService {

    public static final long ESS_PREV_DATA_CNT = 150l;

    private final BuildingPerMinuteRepository buildingRepository;
    private final EssPredictResultRepository essRepository;

    public EssSchFrontPrevResponseDto getEssSchPrevData() {

        List<BuildingPerMinute> bcPrevDataList = buildingRepository.getBuildingConsumptionPrevData(ESS_PREV_DATA_CNT, EssSchThread.buildingPerMinuteId);
        List<EssPredictResult> essPrevDataList = essRepository.getEssSchPrevData(ESS_PREV_DATA_CNT, EssSchThread.buildingPerMinuteId);

        int bcPrevDataCnt = bcPrevDataList.size();
        int essPrevDataCnt = essPrevDataList.size();
        int minCnt = bcPrevDataCnt < essPrevDataCnt ? bcPrevDataCnt : essPrevDataCnt;

        EssSchThread.buildingPerMinuteId += minCnt;

        List<Graph1> graph1 = new ArrayList<>();
        List<Graph2> graph2 = new ArrayList<>();
        List<Graph3> graph3 = new ArrayList<>();

        for(int i=0; i<minCnt; i++) {

            BuildingPerMinute buildingConsumption = bcPrevDataList.get(i);
            EssPredictResult essScheduling = essPrevDataList.get(i);

            graph1.add(getGraph1(buildingConsumption, essScheduling));
            graph2.add(getGraph2(buildingConsumption, essScheduling));
            graph3.add(getGraph3(essScheduling));
        }

        return EssSchFrontPrevResponseDto.builder()
                .graph1(graph1)
                .graph2(graph2)
                .graph3(graph3)
                .build();
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

    private Graph3 getGraph3(EssPredictResult essScheduling) {

        return Graph3.builder()
                .timestamp(getTimestamp(essScheduling))
                .soc(essScheduling.getSoc())
                .build();
    }

    private String getTimestamp(EssPredictResult essScheduling) {
        return essScheduling.getTimestamp().toString();
    }

    public int sumConsumption(BuildingPerMinute bdConsumption) {
        return (int) (bdConsumption.getA_Consumption() + bdConsumption.getB_Consumption() + bdConsumption.getC_Consumption());
    }
}
