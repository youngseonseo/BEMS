package energypa.bems.energy.repository;

import energypa.bems.energy.domain.PredictData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredictDataRepository extends JpaRepository<PredictData, Long> {
}
