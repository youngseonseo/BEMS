package energypa.bems.predictElec.controller;


import energypa.bems.energy.domain.FloorOneHour;
import energypa.bems.predictElec.dto.RequestElecDto;
import energypa.bems.predictElec.dto.ReturnElecDto;
import energypa.bems.predictElec.service.PythonServerConnection;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="predict", description = "AI 전기 사용량 예측량 가져오는 API")
public class RequestElecController {

    private final PythonServerConnection pythonServerConnection;

    @PostMapping(value = "/predict/electAmount")
    public List<ReturnElecDto> requestPredictElec(@RequestBody RequestElecDto requestElecDto) throws IOException {
        return pythonServerConnection.PredictElec(requestElecDto);
    }
}
