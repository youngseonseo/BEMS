package energypa.bems.energy;

import energypa.bems.energy.domain.Building;
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
    CsvReadService csvReadService = new CsvReadService();


    // @PostConstruct
    public void init() {

        buildingInit();
        log.info("building init completed!");
        floorInit();
        log.info("Floor init completed");

    }


    public void buildingInit(){  // 동별 전력 사용량 DB 저장
        if(buildingRepository.findById(100000L).isPresent()){
            return;
        }
        List<Map<String, Object>> maps = csvReadService.readCsv();
        for (Map<String, Object> map : maps) {

            Building building = new Building(Timestamp.valueOf((String) map.get("TIMESTAMP")), Double.valueOf((String) map.get("561_CONSUMPTION(kW)")), Double.valueOf((String)map.get("562_CONSUMPTION(kW)")), Double.valueOf((String) map.get("563_CONSUMPTION(kW)")));
            buildingRepository.save(building);

        }
    }

    public void floorInit(){    // 층별 전력 사용량 DB 저장

    }



}
