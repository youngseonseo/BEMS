package energypa.bems.energy.repository;

import energypa.bems.energy.domain.Building;
import energypa.bems.energy.domain.BuildingPerMinute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingPerMinuteRepository extends JpaRepository<BuildingPerMinute, Long> {
}
