package energypa.bems.energyPattern.controller;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energyPattern.dto.EnergyCompareDto;
import energypa.bems.energyPattern.dto.EnergyConsumptionDto;
import energypa.bems.energyPattern.dto.EnergyPatterDto;
import energypa.bems.energyPattern.dto.EnergySaveCostDto;
import energypa.bems.energyPattern.service.EnergyPatternService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Tag(name = "energyPatter", description = "건물 사용자 에너지 소비 패턴 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EnergyPatternController {

    private final EnergyPatternService energyPatternService;

    @Operation(summary = "건물 사용자 에너지 소비 패턴 분석 요청", description = "층 별 시간대 전력 소비 분석, ess 절약 요금, 다른 세대와 비교 요청")
    @GetMapping("/energy/pattern")
    public EnergyPatterDto monitorByFloor(@CurrentUser UserPrincipal userPrincipal) {
        LocalDate localDate =LocalDate.now();
        EnergyCompareDto energyCompareDto = new EnergyCompareDto(297, 217, -18);
        EnergyConsumptionDto energyConsumptionDto = new EnergyConsumptionDto(7, 28.25, 25.75, 29.00, 17.00, 13, 19);
        EnergySaveCostDto energySaveCostDto = new EnergySaveCostDto(9078, 62064, 210084);
        EnergyPatterDto energyPatternDto = new EnergyPatterDto(localDate, energyConsumptionDto, energySaveCostDto, energyCompareDto);

        return energyPatternDto;
    }
}