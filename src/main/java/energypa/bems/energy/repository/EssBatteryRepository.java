package energypa.bems.energy.repository;

import energypa.bems.energy.domain.EssBattery;
import energypa.bems.ess.dto.BusDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface EssBatteryRepository extends JpaRepository<EssBattery, Long> {

    @Query("select new energypa.bems.ess.dto.BusDto(m.A_bus, m.B_bus, m.C_bus) from EssBattery m " +
            "where m.timestamp>=:startDt and m.timestamp<=:endDt")
    List<BusDto> findA_bus(@Param("startDt") Timestamp startDt, @Param("endDt") Timestamp endDt);

}
