package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;


public interface BuildingPerTenMinuteRepository extends JpaRepository<BuildingPerTenMinute, Long> {


    @Query(value = "select sum(consumption) from buildingpertenminute m where m.timestamp between :startDt and :endDt and m.building =:building and m.floor =:floor", nativeQuery = true)
    Integer findConsumptionByTimestampAndBuildingAndFloor(@Param("startDt") Timestamp startDt, @Param("endDt") Timestamp endDt, @Param("building") int building,  @Param("floor") int floor);


}
