package energypa.bems.unitCost.service;

import energypa.bems.energy.repository.BuildingEnergyPriceRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.login.advice.assertThat.DefaultAssert;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.payload.response.ApiResponse;
import energypa.bems.login.payload.response.Message;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.unitCost.dto.UnitCostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateService {

    private final ApiService apiService;
    private final BuildingPerTenMinuteRepository buildingPerTenMinuteRepository;
    private final MemberRepository memberRepository;
    private final BuildingEnergyPriceRepository buildingEnergyPriceRepository;


    public Double calculateCost(@CurrentUser UserPrincipal userPrincipal) throws IOException {
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(member.isPresent(), "유저가 올바르지 않습니다.");
        Member user = member.get();

        UnitCostDto unitCostDto = new UnitCostDto(LocalDate.now().getYear()-1, LocalDate.now().getMonthValue(), 31, 74);
        Double unitCost = apiService.getApi(unitCostDto); // 단위 전력 요금
//        Double ConsumptionAll = buildingEnergyPriceRepository.findByBuildingAndFloor(user.getBuilding(), user.getFloor());

        return unitCost;

    }
}
