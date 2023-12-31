package energypa.bems.energy;

import energypa.bems.energy.domain.*;
import energypa.bems.energy.repository.*;
import energypa.bems.energy.service.CsvReadService;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.ess.dto.EssPredictRequestDto;
import energypa.bems.predictElec.dto.RequestElecDto;
import energypa.bems.predictElec.dto.ReturnElecDto;
import energypa.bems.predictElec.service.PythonServerConnection;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitEnergyDB {

    private final BuildingPerTenMinuteRepository buildingPerTenMinuteRepository;
    private final BuildingPerMinuteRepository buildingPerMinuteRepository;
    private final BuildingEnergyPriceRepository buildingEnergyPriceRepository;
    private final EssBatteryRepository essBatteryRepository;
    private final FloorOneHourRepository floorOneHourRepository;
    private final FloorOneHourPredictRepository floorOneHourPredictRepository;
    private final TotalOneHourPredictRepository totalOneHourPredictRepository;
    private final PythonServerConnection pythonServerConnection;
    private final TotalOneHourRepository totalOneHourRepository;
    private final EssPredictResultRepository essPredictResultRepository;
    CsvReadService csvReadService = new CsvReadService();


    @PostConstruct
    public void init() throws IOException {


        buildingPerMinuteInit();               // 동 사용량 데이터
        log.info("buildingPerMinute init completed");

        floorInit();                           // 층 사용량 데이터
        log.info("Floor init completed");

        essInit();                             // ess 사용 현황 데이터
        log.info("ess init completed");

        floorOneHourInit();                    // 예측을 위한 가공 데이터
        log.info("floor one hour init completed");

        floorOneHourPredictInit();             // 한시간 단위의 각 빌딩의 층별 예측치
        log.info("floor one hour predict init completed");

        totalPredictInit();                    // 한시간 단위의 전체동 예측치
        log.info("totalPredict init completed");

        totalInit();                          // 한시간 단위의 전체동 사용량
        log.info("totalPredict init completed");

        buildingEnergyPriceInit();             // 전력 요금 저장
        log.info("buildingEnergyPrice init completed");

        essPredictInit();                      // ESS 예측치 저장
        log.info("ESS Predict init completed");


    }

    public void floorInit(){    // 층별 전력 사용량 DB 저장 - [10분 단위] 아파트_층별_소비전력
        if(buildingPerTenMinuteRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[DB]아파트_층별_소비전력_2023-03-16 06.30.00 ~ 2023-08-30 10.30.00.csv");
        for (Map<String, Object> map : maps) {

            BuildingPerTenMinute buildingPerTenMinute = new BuildingPerTenMinute(Timestamp.valueOf((String) map.get("TIMESTAMP")),Integer.valueOf((String) map.get("BUILDING")), Integer.valueOf((String)map.get("FLOOR")), Integer.valueOf((String) map.get("CONSUMPTION(W)")));
            buildingPerTenMinuteRepository.save(buildingPerTenMinute);

        }
    }

    public void buildingPerMinuteInit(){           // [1분 단위] 아파트_동별_소비전력_전력분배
        if(buildingPerMinuteRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[DB]아파트_동별_소비전력_2022-08-27 12.00.00~2022-10-14 17.59.00.csv");
        for (Map<String, Object> map : maps) {

            BuildingPerMinute buildingPerMinute = new BuildingPerMinute(Timestamp.valueOf((String) map.get("TIMESTAMP")), Double.valueOf((String) map.get("561_CONSUMPTION(kW)")), Double.valueOf((String)map.get("562_CONSUMPTION(kW)")), Double.valueOf((String) map.get("563_CONSUMPTION(kW)")));
            buildingPerMinuteRepository.save(buildingPerMinute);

        }
    }

    public void essInit(){           // [1분 단위] 아파트_동별_소비전력_전력분배
        if(essBatteryRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[DB]아파트_전력분배_2022-08-27 12.00.00~2022-10-14 17.59.00.csv");
        for (Map<String, Object> map : maps) {


            EssBattery essBattery = new EssBattery(Timestamp.valueOf((String) map.get("TIMESTAMP")), Integer.valueOf((String) map.get("561_BUS")), Integer.valueOf((String)map.get("562_BUS")), Integer.valueOf((String) map.get("563_BUS")));
            essBatteryRepository.save(essBattery);
        }
    }

    public void floorOneHourInit(){                     // 24시간 AI 예측을 위한 값 저장
        if(floorOneHourRepository.findById(4000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[DB]아파트_층별_소비전력_1시간_2023-03-16 06.00.00 ~ 2023-08-30 10.00.00.csv");
        for (Map<String, Object> map : maps) {

            FloorOneHour floorOneHour = new FloorOneHour(Timestamp.valueOf((String) map.get("TIMESTAMP")),Integer.valueOf((String) map.get("BUILDING")), Integer.valueOf((String)map.get("FLOOR")), Double.valueOf((String) map.get("CONSUMPTION(kW)")));
            floorOneHourRepository.save(floorOneHour);

        }


    }

    public void floorOneHourPredictInit() throws IOException {                // AI의 24시간 예측치 저장
        if(floorOneHourPredictRepository.findById(4000L).isPresent()){
            return;
        }
        Timestamp startDt = Timestamp.valueOf("2023-08-13 17:00:00");
        Timestamp betweenEndDt = new Timestamp(startDt.getYear(),startDt.getMonth(),startDt.getDate(), startDt.getHours(),startDt.getMinutes(),startDt.getSeconds(), startDt.getNanos());
        Timestamp endDt = Timestamp.valueOf("2023-08-29 17:00:00");
        while(betweenEndDt != endDt){
            for (int i=1; i<=4; i++){     // 4 층이므로
                extractedSave(betweenEndDt, i, 561);
                extractedSave(betweenEndDt, i, 562);
                extractedSave(betweenEndDt, i, 563);
            }
            betweenEndDt = new Timestamp(betweenEndDt.getYear(),betweenEndDt.getMonth(),betweenEndDt.getDate()+1, betweenEndDt.getHours(),betweenEndDt.getMinutes(),betweenEndDt.getSeconds(), betweenEndDt.getNanos());
        }
    }

    public void totalPredictInit() {                // 예측치 전체 동 저장
        if(totalOneHourPredictRepository.findById(200L).isPresent()){
            return;
        }
        Timestamp startDt = Timestamp.valueOf("2023-08-13 17:00:00");
        Timestamp betweenEndDt = new Timestamp(startDt.getYear(),startDt.getMonth(),startDt.getDate(), startDt.getHours(),startDt.getMinutes(),startDt.getSeconds(), startDt.getNanos());
        Timestamp endDt = Timestamp.valueOf("2023-08-29 17:00:00");
        while(betweenEndDt != endDt){
            Double totalConsumption = floorOneHourPredictRepository.findConsumptionByTimestamp(betweenEndDt);
            TotalOneHourPredict totalOneHourPredict = new TotalOneHourPredict(betweenEndDt, totalConsumption);
            totalOneHourPredictRepository.save(totalOneHourPredict);
            betweenEndDt = new Timestamp(betweenEndDt.getYear(),betweenEndDt.getMonth(),betweenEndDt.getDate(), betweenEndDt.getHours()+1,betweenEndDt.getMinutes(),betweenEndDt.getSeconds(), betweenEndDt.getNanos());
        }
    }


    public void essPredictInit() throws IOException {                       // ESS 예측치 저장
        if(essPredictResultRepository.findById(2000L).isPresent()){
            return;
        }
        List<BuildingPerMinute> all = buildingPerMinuteRepository.findAll();
        Double soc = 50.0;
        Double batteryPower = 0.0;
        int i=1;
        for (BuildingPerMinute buildingPerMinute : all) {
            if(i<100)
                continue;
            EssPredictRequestDto essPredictRequestDto = new EssPredictRequestDto(buildingPerMinute.getTimestamp(), soc, batteryPower, buildingPerMinute.getA_Consumption(), buildingPerMinute.getB_Consumption(), buildingPerMinute.getC_Consumption());
            EssPredictResult essPredictResult = pythonServerConnection.PredictESS(essPredictRequestDto);
            essPredictResultRepository.save(essPredictResult);
            soc = essPredictResult.getSoc();
            batteryPower = essPredictResult.getBatteryPower();
            i+=1;
        }
    }

    public void totalInit() {                //  전체 동 사용량 저장
        if(totalOneHourRepository.findById(200L).isPresent()){
            return;
        }
        Timestamp startDt = Timestamp.valueOf("2023-08-13 17:00:00");
        Timestamp betweenEndDt = new Timestamp(startDt.getYear(),startDt.getMonth(),startDt.getDate(), startDt.getHours(),startDt.getMinutes(),startDt.getSeconds(), startDt.getNanos());
        Timestamp endDt = Timestamp.valueOf("2023-08-29 17:00:00");
        while(betweenEndDt != endDt){
            Double totalConsumption = floorOneHourRepository.findConsumptionByTimestamp(betweenEndDt);
            TotalOneHour totalOneHour = new TotalOneHour(betweenEndDt, totalConsumption);
            totalOneHourRepository.save(totalOneHour);
            betweenEndDt = new Timestamp(betweenEndDt.getYear(),betweenEndDt.getMonth(),betweenEndDt.getDate(), betweenEndDt.getHours()+1,betweenEndDt.getMinutes(),betweenEndDt.getSeconds(), betweenEndDt.getNanos());
        }
    }

    private void extractedSave(Timestamp betweenEndDt, Integer i, Integer building) throws IOException {
        FloorOneHour floorOneHour = floorOneHourRepository.findByBuildingAndFloorAndTimestamp(building,i,betweenEndDt);
        RequestElecDto requestElecDto = new RequestElecDto(betweenEndDt.toString(),building, i, floorOneHour.getConsumption());
        List<ReturnElecDto> returnElecDtoList = pythonServerConnection.PredictElec(requestElecDto);
        for (ReturnElecDto returnElecDto : returnElecDtoList) {
            FloorOneHourPredict prediction = new FloorOneHourPredict(returnElecDto.getTimestamp(), returnElecDto.getBuilding(), returnElecDto.getFloor(), returnElecDto.getPrediction());
            floorOneHourPredictRepository.save(prediction);
        }
    }

    public void buildingEnergyPriceInit(){           // 한달 단위 빌딩 에너지 사용량 저장 코드 (4월 ~ 8월)
        if(buildingEnergyPriceRepository.findById(15L).isPresent()){
            return;
        }

        Timestamp startDt = Timestamp.valueOf("2023-03-01 00:00:00");
        Timestamp betweenEndDt = new Timestamp(startDt.getYear(),startDt.getMonth()+1,startDt.getDate(), startDt.getHours(),startDt.getMinutes(),startDt.getSeconds(), startDt.getNanos());
        System.out.println("betweenEndDt = " + betweenEndDt);
        Timestamp endDt = Timestamp.valueOf("2023-09-01 00:00:00");


        while(betweenEndDt.before(endDt)) {
            for(int i=1;i<=18;i++){
                Integer consumptionByTimeStamp = buildingPerTenMinuteRepository.findConsumptionByTimestampAndBuildingAndFloor(startDt, betweenEndDt, 561 ,i);
                BuildingEnergyPrice buildingEnergyPrice = new BuildingEnergyPrice(startDt.getMonth(), 561, i, consumptionByTimeStamp);
                buildingEnergyPriceRepository.save(buildingEnergyPrice);
            }
            for(int i=1;i<=24;i++){
                Integer consumptionByTimeStamp = buildingPerTenMinuteRepository.findConsumptionByTimestampAndBuildingAndFloor(startDt, betweenEndDt, 562 ,i);
                BuildingEnergyPrice buildingEnergyPrice = new BuildingEnergyPrice(startDt.getMonth(), 562, i, consumptionByTimeStamp);
                buildingEnergyPriceRepository.save(buildingEnergyPrice);
            }
            for(int i=1;i<=24;i++){
                Integer consumptionByTimeStamp = buildingPerTenMinuteRepository.findConsumptionByTimestampAndBuildingAndFloor(startDt, betweenEndDt, 563 ,i);
                BuildingEnergyPrice buildingEnergyPrice = new BuildingEnergyPrice(startDt.getMonth(), 563, i, consumptionByTimeStamp);
                buildingEnergyPriceRepository.save(buildingEnergyPrice);
            }
            startDt =betweenEndDt;       // 기간을 한달 후로 설정
            betweenEndDt = new Timestamp(startDt.getYear(),startDt.getMonth()+1,startDt.getDate(), startDt.getHours(),startDt.getMinutes(),startDt.getSeconds(), startDt.getNanos());
        }



    }

}
