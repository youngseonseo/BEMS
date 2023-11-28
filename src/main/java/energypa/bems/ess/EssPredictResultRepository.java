package energypa.bems.ess;

import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.ess.dto.BusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface EssPredictResultRepository extends JpaRepository<EssPredictResult, Long> {

    @Query(value = "select * from EssPredictResult limit :limit offset :offset",
           nativeQuery = true)
    List<EssPredictResult> getEssSchPrevData(@Param("limit") long limit, @Param("offset") long offset);

    @Query(value = "select * from EssPredictResult limit 1 offset :offset",
           nativeQuery = true)
    Optional<EssPredictResult> getAiResFromDb(@Param("offset") long offset);

    @Query("select sum(m.batteryPower) from EssPredictResult m " +
            "where m.timestamp>=:startDt and m.timestamp<=:endDt")
    Double findSumBatteryPower(@Param("startDt") Timestamp startDt, @Param("endDt") Timestamp endDt);

    @Query("select m.touRate from EssPredictResult m where m.timestamp =:startDt")
    Double findTouRateByTimestamp(@Param("startDt") Timestamp startDt);
}
