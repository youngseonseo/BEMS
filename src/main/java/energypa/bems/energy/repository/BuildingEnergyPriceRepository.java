package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingEnergyPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BuildingEnergyPriceRepository extends JpaRepository<BuildingEnergyPrice, Long> {


    @Query(value = "select totalConsumption from BuildingEnergyPrice m where m.month =:month and m.building=:building and m.floor=:floor", nativeQuery = true)
    Double findByBuildingAndFloor(@Param("month") int month, @Param("building") int building, @Param("floor") int floor);



}
