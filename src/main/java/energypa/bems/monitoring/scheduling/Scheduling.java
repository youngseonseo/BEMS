package energypa.bems.monitoring.scheduling;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.energy.repository.TotalOneHourPredictRepository;
import energypa.bems.energy.repository.TotalOneHourRepository;
import energypa.bems.monitoring.dto.FloorInfo;
import energypa.bems.monitoring.dto.TotalConsumption;
import energypa.bems.monitoring.repository.SseRepository;
import energypa.bems.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;


@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduling {

    private final MonitoringService monitoringService;
    private final SseRepository sseRepository;
    private final BuildingPerTenMinuteRepository floorRepository;
    private final BuildingPerMinuteRepository buildingRepository;
    private final TotalOneHourRepository totalOneHourRepository;
    private final TotalOneHourPredictRepository totalOneHourPredictRepository;

    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")
    public void scheduleEvery10Min() {

        Map<SseEmitter, FloorInfo> sseEmitterMap = sseRepository.getSseEmitterFloorMap();
        Timestamp now = Timestamp.valueOf(monitoringService.manipulateNowWithSetSec0());

        sseEmitterMap.keySet().stream().forEach((sseEmitter -> {

            FloorInfo floorInfo = sseEmitterMap.get(sseEmitter);
            int building = floorInfo.getBuilding();
            int floor = floorInfo.getFloor();

            BuildingPerTenMinute currentFloorConsumption = floorRepository.findByBuildingAndFloorAndTimestamp(building, floor, now);

            if (currentFloorConsumption != null) {

                try {
                    sseEmitter.send(SseEmitter.event()
                            .name("currentFloorConsumption")
                            .data(currentFloorConsumption));
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }));
    }

    @Scheduled(cron = "0 0 0/1 * * *")
    public void scheduleEveryHour() {

        Map<Long, SseEmitter> sseEmitterMap = sseRepository.getSseEmitterBuildingMap();
        Timestamp now = Timestamp.valueOf(monitoringService.manipulateNowWithSetSec0());
        System.out.println("now is= " + now);
        sseEmitterMap.values().stream().forEach((sseEmitter -> {


            Double buildingConsumption = totalOneHourRepository.getBuildingConsumption(now);
            Double predictConsumption = totalOneHourPredictRepository.getBuildingConsumption(now);

            TotalConsumption currentTotalConsumption = new TotalConsumption(now, (int)Math.round((Double)buildingConsumption*1000),(int)Math.round((Double)predictConsumption*1000));

            if (currentTotalConsumption != null) {

                try {
                    sseEmitter.send(SseEmitter.event()
                            .name("graph1")
                            .data(currentTotalConsumption));
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        }));
    }
}