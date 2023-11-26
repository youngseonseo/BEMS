package energypa.bems.energyPattern.controller;


import energypa.bems.energyPattern.dto.*;
import energypa.bems.energyPattern.service.EnergyPatternService;
import energypa.bems.ess.service.EnergySavingsCalculatorService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
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

    private final MemberRepository memberRepository;
    private final EnergySavingsCalculatorService energySavingsCalculatorService;

    @Operation(summary = "건물 사용자 에너지 소비 패턴 분석 요청", description = "층 별 시간대 전력 소비 분석, ess 절약 요금, 다른 세대와 비교 요청")
    @GetMapping("/energy/pattern")
    public EnergyPatterDto monitorByFloor(@CurrentUser UserPrincipal userPrincipal) {
        Member member = memberRepository.findById(userPrincipal.getId()).get();
        Integer building = member.getBuilding();

        LocalDate localDate =LocalDate.now();
        EnergyCompareDto energyCompareDto = new EnergyCompareDto(297, 217, -18);
        EnergyConsumptionDto energyConsumptionDto = new EnergyConsumptionDto(7, 28.25, 25.75, 29.00, 17.00, 13, 19);
        int i, k; // 빌딩과 세대수 지정
        if(building == 561) {
            i = 0;
            k = 18;
        }
        if (building == 562){
            i = 1;
            k = 24;
        }
        else{
            i = 2;
            k = 24;
        }
        EnergySaveCostDto energySaveCostDto = new EnergySaveCostDto((int)Math.round(energySavingsCalculatorService.CalculateSavingCosts().get(i) * 2 / k), (int)Math.round(energySavingsCalculatorService.CalculateSavingCosts().get(i) * 14 / k), (int)Math.round(energySavingsCalculatorService.CalculateSavingCosts().get(i) * 60 / k));
        EnergyPatterDto energyPatternDto = new EnergyPatterDto(localDate, energyConsumptionDto, energySaveCostDto, energyCompareDto);
        return energyPatternDto;
    }


    @Operation(summary = "건물 에너지 관리 분석 요청", description = "아파트 별 ESS 배터리 절약 효과, 에너지, 전체 동 에너지 소비 패턴 분석")
    @GetMapping("/energy/pattern/building")
    public EnergyPatterBuildingDto monitorByBuilding(@CurrentUser UserPrincipal userPrincipal) {
        LocalDate localDate =LocalDate.now();
        EnergyConsumptionDto energyConsumptionDto = new EnergyConsumptionDto(900, 28.25, 25.75, 29.00, 17.00, 13, 19);
        EnergySaveCostDto energySaveCostDto1 = new EnergySaveCostDto(energySavingsCalculatorService.CalculateSavingCosts().get(0)*2, energySavingsCalculatorService.CalculateSavingCosts().get(0)*14, energySavingsCalculatorService.CalculateSavingCosts().get(0)*60);
        EnergySaveCostDto energySaveCostDto2 = new EnergySaveCostDto(energySavingsCalculatorService.CalculateSavingCosts().get(1)*2, energySavingsCalculatorService.CalculateSavingCosts().get(1)*14, energySavingsCalculatorService.CalculateSavingCosts().get(1)*60);
        EnergySaveCostDto energySaveCostDto3 = new EnergySaveCostDto(energySavingsCalculatorService.CalculateSavingCosts().get(2)*2, energySavingsCalculatorService.CalculateSavingCosts().get(2)*14, energySavingsCalculatorService.CalculateSavingCosts().get(2)*60);
        EnergyPatterBuildingDto energyPatternDto = new EnergyPatterBuildingDto(localDate, energyConsumptionDto,energySaveCostDto1, energySaveCostDto2, energySaveCostDto3);

        return energyPatternDto;
    }
}