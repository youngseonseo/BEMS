package energypa.bems.monitoring.service;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.monitoring.dto.EachConsumption;
import energypa.bems.monitoring.dto.MonitorBuildingResponse;
import energypa.bems.monitoring.dto.TotalConsumption;
import energypa.bems.monitoring.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final BuildingPerTenMinuteRepository floorRepository;
    private final BuildingPerMinuteRepository buildingRepository;
    private final SseRepository sseRepository;

    public SseEmitter formSseConnectionForFloor(int building, int floor, @CurrentUser UserPrincipal userPrincipal) {

        // sse 연결
        SseEmitter sseEmitter = new SseEmitter();

        // 생성한 SseEmitter 객체 저장
        sseRepository.saveForFloor(sseEmitter, building, floor, userPrincipal);

        // sse 연결 후 dummy data 전송
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("dummy data to prevent 503"));
        } catch (IOException e) {
            log.error("", e);
        }

        return sseEmitter;
    }

    public LocalDateTime manipulateNowForFloor() {

        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(7L);
    }

    public LocalDateTime manipulateNowWithSetSec0() {

        return manipulateNowForFloor().withSecond(0).withNano(0);
    }

    public List<BuildingPerTenMinute> getPrevFloorInfo(int building, int floor) {

        Timestamp now = Timestamp.valueOf(manipulateNowForFloor());
        return floorRepository.getPrevFloorConsumption(building, floor, now);
    }

    public SseEmitter formSseConnectionForBuilding(@CurrentUser UserPrincipal userPrincipal) {

        // sse 연결
        SseEmitter sseEmitter = new SseEmitter();

        // 생성한 SseEmitter 객체 저장
        sseRepository.saveForBuilding(sseEmitter, userPrincipal);

        // sse 연결 후 dummy data 전송
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("dummy data to prevent 503"));
        } catch (IOException e) {
            log.error("", e);
        }

        return sseEmitter;
    }

    public LocalDateTime manipulateNowForBuilding() {

        LocalDateTime now = LocalDateTime.now();
        now = now.minusYears(1L);
        return now.minusMonths(3L);
    }

    public String getYesterday() {
        LocalDateTime todayDate = manipulateNowForBuilding();
        return todayDate.toLocalDate().minusDays(1L).toString();
    }

    public String getLastWeek() {
        LocalDateTime todayDate = manipulateNowForBuilding();
        int yoilNum = todayDate.getDayOfWeek().getValue();
        return todayDate.toLocalDate().minusDays(yoilNum).toString();
    }

    public String getLastMonth() {
        LocalDateTime todayDate = manipulateNowForBuilding();
        todayDate = todayDate.minusMonths(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return todayDate.format(formatter);
    }

    public MonitorBuildingResponse getPrevBuildingInfo(String duration) {

        Timestamp now = Timestamp.valueOf(manipulateNowForBuilding());

        List<TotalConsumption> graph1 = buildingRepository.getPrevBuildingConsumption(now).stream()
                .map(row -> new TotalConsumption((Timestamp) row[0], (Double) row[1]))
                .collect(Collectors.toList());

        MonitorBuildingResponse monitorBuildingResponse = null;
        if (duration.equals("date")) {

            EachConsumption graph2 = buildingRepository.getYesterdayConsumption(getYesterday()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList())
                    .get(0);

            List<EachConsumption> graph3 = buildingRepository.getPrevDailyConsumption(getYesterday()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList());

            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        else if (duration.equals("week")) {

            EachConsumption graph2 = buildingRepository.getLastWeekConsumption(getLastWeek()).stream()
                    .map(row ->  new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList())
                    .get(0);

            List<EachConsumption> graph3 = buildingRepository.getWeeklyConsumption(getLastWeek()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList());

            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        else if (duration.equals("month")) {

            EachConsumption graph2 = buildingRepository.getLastMonthConsumption(getLastMonth()).stream()
                    .map(row ->  new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList())
                    .get(0);

            List<EachConsumption> graph3 = buildingRepository.getMonthlyConsumption(getLastMonth()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (Double) row[1], (Double) row[2], (Double) row[3]))
                    .collect(Collectors.toList());

            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        return monitorBuildingResponse;
    }
}