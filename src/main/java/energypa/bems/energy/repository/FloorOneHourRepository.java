package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.domain.FloorOneHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface FloorOneHourRepository extends JpaRepository<FloorOneHour, Long> {   // 각 동의 실제 사용량 저장 (한 시간 단위)

    FloorOneHour findByBuildingAndFloorAndTimestamp(Integer building, Integer floor, Timestamp now);

    @Query(value = "select sum(consumption) from FloorOneHour m where m.timestamp =:now", nativeQuery = true)
    Double findConsumptionByTimestamp( @Param("now") Timestamp now);
}
