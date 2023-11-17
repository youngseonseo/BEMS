package energypa.bems.cost.controller;

import energypa.bems.cost.dto.TotalCostDto;
import energypa.bems.cost.dto.UnitCostDto;
import energypa.bems.cost.service.ApiService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.cost.service.CalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@Tag(name="UserBill", description = "전력 사용량, 전력 비용 2달전, 1달전, 이번달 구하는 API")
@RequestMapping("/api")
public class UnitCostController {


    private final CalculateService calculateService;
    private final ApiService apiService;

    @Operation(method = "get", summary = "해당 날짜의 시,군,구를 고려한 전력용 단위 요금")
    @GetMapping("/unitCost")       // 전력 단위 요금
    public Double unitCost(UnitCostDto unitCostDto) throws IOException {
        return apiService.getApi(unitCostDto);
    }

    @Operation(method = "get", summary = "2달전, 1달전, 이번달 전력 요금(원), 전력 사용량(kW) API ")
    @GetMapping("/bill")       // 전력 단위 요금
    public TotalCostDto energyPriceByMonth(@CurrentUser UserPrincipal userPrincipal) throws IOException {
        return calculateService.calculateCost(userPrincipal);
    }


}
