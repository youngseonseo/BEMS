package energypa.bems.ess;

import energypa.bems.ess.domain.EssPredictResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EssPredictResultRepository extends JpaRepository<EssPredictResult, Long> {

    @Query(value = "select * from EssPredictResult limit 1 offset :offset",
           nativeQuery = true)
    Optional<EssPredictResult> getAiResFromDb(@Param("offset") long offset);
}
