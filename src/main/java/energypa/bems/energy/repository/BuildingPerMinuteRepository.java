package energypa.bems.energy.repository;

import energypa.bems.energy.domain.Building;
import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.ess.dto.BusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface BuildingPerMinuteRepository extends JpaRepository<BuildingPerMinute, Long> {
    @Query("select new energypa.bems.ess.dto.BusDto(m.A_bus, m.B_bus, m.C_bus) from BuildingPerMinute m " +
            "where m.timestamp>=:startDt and m.timestamp<=:endDt")
    List<BusDto> findA_bus(@Param("startDt") Timestamp startDt, @Param("endDt") Timestamp endDt);

}
