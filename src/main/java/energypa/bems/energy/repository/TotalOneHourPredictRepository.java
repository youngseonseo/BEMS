package energypa.bems.energy.repository;

import energypa.bems.energy.domain.TotalOneHourPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TotalOneHourPredictRepository extends JpaRepository<TotalOneHourPredict, Long> {

    @Query( value = "select  bpm.timestamp, bpm.consumption from TotalOneHourPredict bpm where date(bpm.timestamp) between date_sub(:now, interval 5 DAY) and date_add(:now, interval 1 DAY)",
            nativeQuery = true
    )
    List<Object[]> getPrevBuildingConsumption(@Param("now") Timestamp now);


    @Query("select bpm.consumption from TotalOneHourPredict bpm where bpm.timestamp=:timestamp")
    Double getBuildingConsumption(@Param("timestamp") Timestamp timestamp);

}
