package energypa.bems.ess;

import energypa.bems.ess.domain.EssPredictResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssPredictResultRepository extends JpaRepository<EssPredictResult, Long> {
}
