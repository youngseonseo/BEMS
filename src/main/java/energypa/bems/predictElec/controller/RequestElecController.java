package energypa.bems.predictElec.controller;


import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.predictElec.dto.RequestElecDto;
import energypa.bems.predictElec.dto.ReturnElecDto;
import energypa.bems.predictElec.service.PythonServerConnection;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="predict", description = "AI 전기 사용량 예측량 가져오는 API")
public class RequestElecController {

    private final PythonServerConnection pythonServerConnection;

    @PostMapping(value = "/predict/electAmount")
    public ReturnElecDto requestPredictElec(@RequestBody RequestElecDto requestElecDto) {
        System.out.println("requestElecDto.getTimestamp() = " + requestElecDto.getTimestamp());
        return pythonServerConnection.PredictElec(requestElecDto);
    }
}
