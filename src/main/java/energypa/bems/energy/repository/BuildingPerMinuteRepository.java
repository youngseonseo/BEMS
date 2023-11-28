package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.ess.dto.BusDto;
import energypa.bems.ess.dto.RateConsumptionDto;
import energypa.bems.monitoring.dto.Graph1Dto;
import energypa.bems.monitoring.dto.TotalConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BuildingPerMinuteRepository extends JpaRepository<BuildingPerMinute, Long> {

    // graph1
    @Query( value = "select bpm.timestamp, bpm.A_Consumption + bpm.B_Consumption + bpm.C_Consumption from BuildingPerMinute bpm where date(bpm.timestamp) between date_sub(:now, interval 5 DAY) and :now",
            nativeQuery = true
    )
    List<Object[]> getPrevBuildingConsumption(@Param("now") Timestamp now);

    // 1분마다 보내지는 query
    @Query("select (bpm.A_Consumption + bpm.B_Consumption + bpm.C_Consumption) from BuildingPerMinute bpm where bpm.timestamp=:timestamp")
    Double getBuildingConsumption(@Param("timestamp") Timestamp timestamp);

    // graph2
    @Query(
            value = "select date(bpm.timestamp), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date(bpm.timestamp) = :yesterday",
            nativeQuery = true
    )
    List<Object[]> getYesterdayConsumption(@Param("yesterday") String yesterday);

    @Query(
            value = "select date(:date), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date(bpm.timestamp) between date_sub(:date, interval 6 day) and :date",
            nativeQuery = true
    )
    List<Object[]> getLastWeekConsumption(@Param("date") String date);

    @Query(
            value = "select date(date_format(bpm.timestamp, '%Y-%m')), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date_format(bpm.timestamp, '%Y-%m') = :lastMonth",
            nativeQuery = true
    )
    List<Object[]> getLastMonthConsumption(@Param("lastMonth") String lastMonth);

    // graph3
    @Query(
            value = "select date(bpm.timestamp), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date(bpm.timestamp) >= :lastWeek and date(bpm.timestamp) <= :yesterday group by date(bpm.timestamp)",
            nativeQuery = true
    )
    List<Object[]> getPrevDailyConsumption(@Param("lastWeek") String lastWeek, @Param("yesterday") String yesterday);


    // ESS 배터리 절약 요금 계산을 위한 query
    @Query(
            value = "select new energypa.bems.ess.dto.RateConsumptionDto(sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption)) from BuildingPerMinute bpm where bpm.timestamp>=:startDt and bpm.timestamp<=:endDt"
    )
    RateConsumptionDto getRateConsumption(@Param("startDt") Timestamp startDt, @Param("endDt") Timestamp endDt);

    /**
    @Query(
            value = "select date(date_sub(timestamp, interval (dayofweek(timestamp)-7) DAY)) as end, " +
                    "sum(a_consumption), " +
                    "sum(b_consumption), " +
                    "sum(c_consumption) " +
                    "from BuildingPerMinute " +
                    "where timestamp < :lastWeek and timestamp > :lastMonth " +
                    "group by date_format(timestamp, '%Y%U')",
            nativeQuery = true
    )
    List<Object[]> getWeeklyConsumption(@Param("lastMonth") String lastMonth, @Param("lastWeek") String lastWeek);

    @Query(
            value = "select date(date_format(bpm.timestamp, '%Y-%m')), sum(bpm.A_Consumption), sum(bpm.B_Consumption), sum(bpm.C_Consumption) from BuildingPerMinute bpm where date_format(bpm.timestamp, '%Y-%m') <= :lastMonth group by date_format(bpm.timestamp, '%Y-%m')",
            nativeQuery = true
    )
    List<Object[]> getMonthlyConsumption(@Param("lastMonth") String lastMonth);

    */

    @Query(value = "select * from BuildingPerMinute limit 150 offset :offset",
            nativeQuery = true)
    List<BuildingPerMinute> getBuildingConsumptionPrevData(@Param("offset") long offset);
}
