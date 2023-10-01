package energypa.bems.unitCost.controller;

import energypa.bems.unitCost.domain.UnitCost;
import energypa.bems.unitCost.dto.UnitCostDto;
import energypa.bems.unitCost.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Tag(name="unitCost", description = "전력 단위 요금 불러오는 API")
public class UnitCostController {

    private final ApiService apiService;

    @Operation(method = "get", summary = "해당 시간의 단위 주택용 전력 요금 추출 API")
    @GetMapping("/unitCost")       // 전력 단위 요금
    public UnitCost circus(@ModelAttribute("unitCostDto") UnitCostDto unitCostDto) throws IOException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());


        return apiService.getApi(unitCostDto);
    }



}
