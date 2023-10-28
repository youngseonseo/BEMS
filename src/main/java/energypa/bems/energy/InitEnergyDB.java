package energypa.bems.energy;

import energypa.bems.energy.domain.Building;
import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.energy.repository.BuildingRepository;
import energypa.bems.energy.service.CsvReadService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitEnergyDB {

    private final BuildingRepository buildingRepository;
    private final BuildingPerTenMinuteRepository buildingPerTenMinuteRepository;
    private final BuildingPerMinuteRepository buildingPerMinuteRepository;
    CsvReadService csvReadService = new CsvReadService();


    @PostConstruct
    public void init() {

        buildingInit();
        log.info("building init completed!");
        floorInit();
        log.info("Floor init completed");
        buildingPerMinuteInit();
        log.info("buildingPerMinute init completed");

    }


    public void buildingInit(){  // 동별 전력 사용량 DB 저장 - 아파트_동별_소비전력_전력분배
        if(buildingRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/아파트_동별_소비전력_전력분배_2022-07-18~2023-08-30.csv");
        for (Map<String, Object> map : maps) {

            Building building = new Building(Timestamp.valueOf((String) map.get("TIMESTAMP")), Double.valueOf((String) map.get("561_CONSUMPTION(kW)")), Double.valueOf((String)map.get("562_CONSUMPTION(kW)")), Double.valueOf((String) map.get("563_CONSUMPTION(kW)")), Double.valueOf((String) map.get("561_bus")), Double.valueOf((String)map.get("562_bus")), Double.valueOf((String) map.get("563_bus")));
            buildingRepository.save(building);

        }
    }

    public void floorInit(){    // 층별 전력 사용량 DB 저장 - [10분 단위] 아파트_층별_소비전력
        if(buildingPerTenMinuteRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[10분 단위]아파트_층별_소비전력_2023-03-16 06.30.00 ~ 2023-08-30 10.30.00.csv");
        for (Map<String, Object> map : maps) {

            BuildingPerTenMinute buildingPerTenMinute = new BuildingPerTenMinute(Timestamp.valueOf((String) map.get("TIMESTAMP")),Integer.valueOf((String) map.get("BUILDING")), Integer.valueOf((String)map.get("FLOOR")), Integer.valueOf((String) map.get("CONSUMPTION(W)")));
            buildingPerTenMinuteRepository.save(buildingPerTenMinute);

        }

    }

    public void buildingPerMinuteInit(){           // [1분 단위] 아파트_동별_소비전력_전력분배
        if(buildingPerMinuteRepository.findById(10000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv("preprocessed_data/[1분 단위]아파트_동별_소비전력_전력분배_2022-07-18 00.00.00~2023-08-30 10.39.00.csv");
        for (Map<String, Object> map : maps) {

            BuildingPerMinute buildingPerMinute = new BuildingPerMinute(Timestamp.valueOf((String) map.get("TIMESTAMP")), Double.valueOf((String) map.get("561_CONSUMPTION(kW)")), Double.valueOf((String)map.get("562_CONSUMPTION(kW)")), Double.valueOf((String) map.get("563_CONSUMPTION(kW)")), Double.valueOf((String) map.get("561_bus")), Double.valueOf((String)map.get("562_bus")), Double.valueOf((String) map.get("563_bus")));
            buildingPerMinuteRepository.save(buildingPerMinute);

        }


    }



}
