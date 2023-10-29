package energypa.bems.energy.repository;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuildingPerTenMinuteRepository extends JpaRepository<BuildingPerTenMinute, Long> {
}
