package energypa.bems.energyPattern.controller;


import energypa.bems.energyPattern.dto.*;
import energypa.bems.energyPattern.service.EnergyPatternService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



import java.time.LocalDate;


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


    @Operation(summary = "건물 에너지 관리 분석 요청", description = "아파트 별 ESS 배터리 절약 효과, 에너지, 전체 동 에너지 소비 패턴 분석")
    @GetMapping("/energy/pattern/building")
    public EnergyPatterBuildingDto monitorByBuilding(@CurrentUser UserPrincipal userPrincipal) {
        LocalDate localDate =LocalDate.now();
        EnergyConsumptionDto energyConsumptionDto = new EnergyConsumptionDto(900, 28.25, 25.75, 29.00, 17.00, 13, 19);
        EnergySaveCostDto energySaveCostDto1 = new EnergySaveCostDto(109078, 862064, 3500084);
        EnergySaveCostDto energySaveCostDto2 = new EnergySaveCostDto(89078, 622064, 2700043);
        EnergySaveCostDto energySaveCostDto3 = new EnergySaveCostDto(49078, 482064, 1660289);
        EnergyPatterBuildingDto energyPatternDto = new EnergyPatterBuildingDto(localDate, energyConsumptionDto,energySaveCostDto1, energySaveCostDto2, energySaveCostDto3);

        return energyPatternDto;
    }
}