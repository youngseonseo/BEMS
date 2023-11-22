package energypa.bems.energy.repository;

import energypa.bems.energy.domain.FloorOneHour;
import energypa.bems.energy.domain.FloorOneHourPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface FloorOneHourPredictRepository extends JpaRepository<FloorOneHourPredict, Long> {  // 각 동의 예측치 저장(한 시간 단위)

    @Query(value = "select sum(consumption) from FloorOneHourPredict m where m.timestamp =:now", nativeQuery = true)
    Double findConsumptionByTimestamp( @Param("now") Timestamp now);

}
