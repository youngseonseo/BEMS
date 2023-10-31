package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingEnergyPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingEnergyPriceRepository extends JpaRepository<BuildingEnergyPrice, Long> {

//    Double findByBuildingAndFloor(Integer building, Integer floor);


}
