package energypa.bems.ess.controller;


import energypa.bems.ess.service.EssService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="ESS 스케줄링 현재 상황", description = "현재 ESS 스케줄링(전력량 분배)을 보여주는 API")
public class EssController {

    private final EssService essService;

    @Operation(method = "post", summary = "ESS 모니터링 서비스 API(30초 단위)")
    @PostMapping("/ess/monitor")
    public ResponseEntity<?> EssMonitorController(@CurrentUser UserPrincipal userPrincipal) {
        essService.essMonitorService(userPrincipal);
        return ResponseEntity.ok(true);
    }

}
