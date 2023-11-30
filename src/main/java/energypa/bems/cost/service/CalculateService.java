package energypa.bems.cost.service;

import energypa.bems.cost.dto.TotalCostDto;
import energypa.bems.energy.repository.BuildingEnergyPriceRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.login.advice.assertThat.DefaultAssert;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.cost.dto.UnitCostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateService {

    private final ApiService apiService;
    private final MemberRepository memberRepository;
    private final BuildingEnergyPriceRepository buildingEnergyPriceRepository;
    private final BuildingPerTenMinuteRepository buildingPerTenMinuteRepository;


    public TotalCostDto calculateCost(@CurrentUser UserPrincipal userPrincipal) throws IOException {   // 전력 요금 계산 서비스
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(member.isPresent(), "유저가 올바르지 않습니다.");
        Member user = member.get();

        List<Integer> priceList = new ArrayList<>();
        List<Integer> costList = new ArrayList<>();

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime currentLocal = LocalDateTime.of(current.getYear(), current.getMonthValue(), current.getDayOfMonth(), current.getHour(), ((current.getMinute())/10)*10 );

        // 전달, 전전달 전력 요금 계산
        for(int i=0;i<2; i++){
            UnitCostDto unitCostDto = new UnitCostDto(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-8+i, 31, 74);
            Double unitCost = apiService.getApi(unitCostDto); // 단위 전력 요금
            Double ConsumptionAll = buildingEnergyPriceRepository.findByBuildingAndFloor(LocalDate.now().getMonthValue()-8+i,user.getBuilding(), user.getFloor());

            costList.add((int)(ConsumptionAll/100/60));    // kW기준 전력 사용량 저장
            priceList.add((int)(ConsumptionAll * unitCost)/100/60);   // 원 기준 전력 요금 저장 -> 10분 단위와 kWh이므로 100을 나누어 주어야 함 (*10 /1000)
        }


        // 이번 달 실시간 전력 사용량 계산
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Timestamp startDt = new Timestamp(now.getYear(),now.getMonth()-6,1, 0,0,0, 0);
        Timestamp betweenEndDt = new Timestamp(now.getYear(),now.getMonth()-6,now.getDate(), now.getHours(),now.getMinutes(),now.getSeconds(), now.getNanos());

        // 이번 달 전력 단위 요금 계산
        UnitCostDto unitCostDto = new UnitCostDto(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-6, 31, 74);
        Double unitCost = apiService.getApi(unitCostDto);

        // 이번 달 실시간 전력 요금 계산
        Integer consumptionByTimeStamp = buildingPerTenMinuteRepository.findConsumptionByTimestampAndBuildingAndFloor(startDt, betweenEndDt, user.getBuilding() ,user.getFloor());
        priceList.add((int)(consumptionByTimeStamp * unitCost)/100/60);
        costList.add((int)(consumptionByTimeStamp/100/60));

        return new TotalCostDto(currentLocal,priceList,costList);

    }


}
