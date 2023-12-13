package energypa.bems.monitoring.service;

import energypa.bems.chat.entity.Matching;
import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.energy.repository.TotalOneHourPredictRepository;
import energypa.bems.energy.repository.TotalOneHourRepository;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.monitoring.dto.*;
import energypa.bems.monitoring.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    public static final long TIMEOUT = 60L * 1000 * 60 * 100;  // 100분
    private final BuildingPerTenMinuteRepository floorRepository;
    private final BuildingPerMinuteRepository buildingRepository;
    private final SseRepository sseRepository;
    private final TotalOneHourRepository totalOneHourRepository;
    private final TotalOneHourPredictRepository totalOneHourPredictRepository;

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
        return now.minusMonths(15L);
    }

    public LocalDateTime manipulateNowWithSetSec0() {

        return manipulateNowForBuilding().withSecond(0).withNano(0);
    }

    public List<BuildingPerTenMinute> getPrevFloorInfo(int building, int floor) {

        Timestamp now = Timestamp.valueOf(manipulateNowForFloor());
        return floorRepository.getPrevFloorConsumption(building, floor, now);
    }

    public SseEmitter formSseConnectionForBuilding(@CurrentUser UserPrincipal userPrincipal) {

        // sse 연결
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
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
        now = now.minusYears(0L);
        now = now.minusMonths(3L);
        return now.minusDays(15L).withMinute(0).withSecond(0).withNano(0);
    }
    public LocalDateTime manipulateNowForBuildingforGraph2() {

        LocalDateTime now = LocalDateTime.now();
        now = now.minusYears(1L);
        now = now.minusMonths(2L);
        return now.plusDays(1L);
    }

    public String getYesterday() {
        LocalDateTime todayDate = manipulateNowForBuildingforGraph2();
        return todayDate.toLocalDate().minusDays(1L).toString();
    }
    public String getOneWeekAgo() {
        LocalDateTime todayDate = manipulateNowForBuildingforGraph2();
        return todayDate.toLocalDate().minusDays(7L).toString();
    }

    public String getLastWeek() {
        LocalDateTime todayDate = manipulateNowForBuildingforGraph2();
        int yoilNum = todayDate.getDayOfWeek().getValue();

        return todayDate.toLocalDate().minusDays(yoilNum).toString();
    }

    public String getLastMonth() {
        LocalDateTime todayDate = manipulateNowForBuildingforGraph2();
        todayDate = todayDate.minusMonths(1L);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return todayDate.format(formatter);
    }

    public List<EachConsumption> makeImplicitValue(int N){

        List<EachConsumption> eachConsumptions = new ArrayList<>();
        for(int i=0;i<4;i++){
            EachConsumption eachConsumption = new EachConsumption(new Date(122,8,i+2), N + i * 4000  , N-100000 - 2045*i  ,N-200000+i*3030 );
            eachConsumptions.add(eachConsumption);
        }
        for(int i=4;i<7;i++){
            EachConsumption eachConsumption = new EachConsumption(new Date(122,9,i-3), N - i * 2500  , N-100000 + 1700 * i ,N-150000 - i*2900 );
            eachConsumptions.add(eachConsumption);
        }
        return eachConsumptions;
    }

    public List<EachConsumption> makeImplicitValue2(int N){

        List<EachConsumption> eachConsumptions = new ArrayList<>();
        for(int i=0;i<4;i++){
            EachConsumption eachConsumption = new EachConsumption(new Date(122,i+2,-1), N + 140000 - i * 4800  , N + 150000 - 5600 *i  ,N-200000 - i*3030 );
            eachConsumptions.add(eachConsumption);
        }
        for(int i=4;i<7;i++){
            EachConsumption eachConsumption = new EachConsumption(new Date(122,i+2,-1), N +  i * 6500  , N-100000 + 6000 * i ,N-170000 + i*2900 );
            eachConsumptions.add(eachConsumption);
        }
        return eachConsumptions;
    }

    public MonitorBuildingResponse getPrevBuildingInfo(String duration) {

        Timestamp now = Timestamp.valueOf(manipulateNowForBuilding());
        System.out.println("now = " + now);

        List<ImplicitTotalConsumption> list = totalOneHourRepository.getPrevBuildingConsumption(now).stream()
                .map(row -> new ImplicitTotalConsumption((Timestamp) row[0], (int)Math.round((Double) row[1]*1000)))
                .collect(Collectors.toList());


        List<ImplicitTotalConsumption> predictList = totalOneHourPredictRepository.getPrevBuildingConsumption(now).stream()
                .map(row -> new ImplicitTotalConsumption((Timestamp) row[0], (int)Math.round((Double) row[1]*1000)))
                .collect(Collectors.toList());


        List<TotalConsumption> graph1 = new ArrayList<>();
        for (int i=0; i< list.size(); i++){
            TotalConsumption totalConsumption = new TotalConsumption(list.get(i).getTimestamp(), list.get(i).getTotalConsumption(),predictList.get(i).getTotalConsumption());
            graph1.add(totalConsumption);
        }
        for (int i=24*5; i<24*6; i++){
            TotalConsumption totalConsumption = new TotalConsumption(predictList.get(i).getTimestamp(), 0,predictList.get(i).getTotalConsumption());
            graph1.add(totalConsumption);
        }


        MonitorBuildingResponse monitorBuildingResponse = null;
        if (duration.equals("date")) {
            System.out.println("getYesterday() = " + getYesterday());
            EachConsumption graph2 = buildingRepository.getYesterdayConsumption(getYesterday()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList())
                    .get(0);


            List<EachConsumption> graph3 = buildingRepository.getPrevDailyConsumption(getOneWeekAgo(), getYesterday()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList());



            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        else if (duration.equals("week")) {

            EachConsumption graph2 = buildingRepository.getLastWeekConsumption(getLastWeek()).stream()
                    .map(row ->  new EachConsumption((Date) row[0],(int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList())
                    .get(0);


/**            List<EachConsumption> graph3 = buildingRepository.getWeeklyConsumption(getLastMonth(),getLastWeek()).stream()
                    .map(row -> new EachConsumption((Date) row[0],(int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList());
**/
            List<EachConsumption> graph3 = makeImplicitValue(graph2.totalAConsumption);

            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        else if (duration.equals("month")) {

            EachConsumption graph2 = buildingRepository.getLastMonthConsumption(getLastMonth()).stream()
                    .map(row ->  new EachConsumption((Date) row[0], (int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList())
                    .get(0);
/**
            List<EachConsumption> graph3 = buildingRepository.getMonthlyConsumption(getLastMonth()).stream()
                    .map(row -> new EachConsumption((Date) row[0], (int) Math.round((Double)row[1]), (int) Math.round((Double)row[2]), (int) Math.round((Double)row[3])))
                    .collect(Collectors.toList());
**/

            List<EachConsumption> graph3 = makeImplicitValue2(graph2.totalAConsumption);

            monitorBuildingResponse = MonitorBuildingResponse.builder()
                    .graph1(graph1)
                    .graph2(graph2)
                    .graph3(graph3)
                    .build();
        }
        return monitorBuildingResponse;
    }
}