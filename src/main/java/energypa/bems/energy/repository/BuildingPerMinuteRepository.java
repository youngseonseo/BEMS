package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.monitoring.dto.EachConsumption;
import energypa.bems.monitoring.dto.TotalConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BuildingPerMinuteRepository extends JpaRepository<BuildingPerMinute, Long> {

    // graph1
    @Query("select bpm.timestamp, bpm.A_Consumption + bpm.B_Consumption + bpm.C_Consumption from BuildingPerMinute bpm where bpm.timestamp <= :now")
    List<TotalConsumption> getPrevBuildingConsumption(@Param("now") Timestamp now);

    @Query("select bpm.timestamp, bpm.A_Consumption + bpm.B_Consumption + bpm.C_Consumption from BuildingPerMinute bpm where bpm.timestamp=:timestamp")
    TotalConsumption getBuildingConsumption(@Param("timestamp") Timestamp timestamp);

    // graph2
    @Query(
            value = "select date(bpm.timestamp), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date(bpm.timestamp) = :yesterday",
            nativeQuery = true
    )
    EachConsumption getYesterdayConsumption(@Param("yesterday") String yesterday);

    // graph3
    @Query(
            value = "select date(bpm.timestamp), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date(bpm.timestamp) <= :yesterday group by date(bpm.timestamp)",
            nativeQuery = true
    )
    List<EachConsumption> getPrevDailyConsumption(@Param("yesterday") String yesterday);
}
