package energypa.bems.energy.repository;

import energypa.bems.energy.domain.TotalOneHour;
import energypa.bems.energy.domain.TotalOneHourPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TotalOneHourRepository extends JpaRepository<TotalOneHour, Long> {

    // 1분마다 보내지는 query
    @Query("select bpm.consumption from TotalOneHour bpm where bpm.timestamp=:timestamp")
    Double getBuildingConsumption(@Param("timestamp") Timestamp timestamp);


    // graph1
    @Query( value = "select  bpm.timestamp, bpm.consumption from TotalOneHour bpm where date(bpm.timestamp) between date_sub(:now, interval 5 DAY) and :now",
            nativeQuery = true
    )
    List<Object[]> getPrevBuildingConsumption(@Param("now") Timestamp now);


}
