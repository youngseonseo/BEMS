package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BuildingPerTenMinuteRepository extends JpaRepository<BuildingPerTenMinute, Long> {

    @Query("select bptm from BuildingPerTenMinute bptm where bptm.building=:building and bptm.floor=:floor and bptm.timestamp <= :now")
    List<BuildingPerTenMinute> getPrevFloorConsumption(@Param("building") int building, @Param("floor") int floor, @Param("now") Timestamp now);

    BuildingPerTenMinute findByBuildingAndFloorAndTimestamp(int building, int floor, Timestamp now);
}
