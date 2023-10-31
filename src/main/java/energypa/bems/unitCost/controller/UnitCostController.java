package energypa.bems.unitCost.controller;

import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.login.advice.assertThat.DefaultAssert;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.unitCost.domain.UnitCost;
import energypa.bems.unitCost.dto.UnitCostDto;
import energypa.bems.unitCost.service.ApiService;
import energypa.bems.unitCost.service.CalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Tag(name="unitCost", description = "전력 단위 요금 불러오는 API")
public class UnitCostController {


    private final CalculateService calculateService;

    @Operation(method = "get", summary = "유저가 사용하는 층의 전력 요금 추출 API")
    @GetMapping("/unitCost")       // 전력 단위 요금
    public Double unitCost(@CurrentUser UserPrincipal userPrincipal) throws IOException {
        return calculateService.calculateCost(userPrincipal);
    }

}
