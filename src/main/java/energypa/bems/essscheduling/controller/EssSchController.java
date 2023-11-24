package energypa.bems.essscheduling.controller;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.essscheduling.dto.front.EssSchFrontResponseDto;
import energypa.bems.essscheduling.dto.front.Graph1;
import energypa.bems.essscheduling.dto.front.Graph2;
import energypa.bems.essscheduling.dto.front.Graph3;
import energypa.bems.essscheduling.thread.EssSchThread;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static energypa.bems.notification.controller.NotificationController.sseEmitters;

@Tag(name = "ESS battery scheduling 모니터링", description = "ESS battery scheduling API")
@RestController
@RequestMapping("/api/ess")
@RequiredArgsConstructor
public class EssSchController {

    private final BuildingPerMinuteRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final EssPredictResultRepository essRepository;

    @Operation(summary = "ESS battery scheduling 모니터링 요청", description = "유저가 ESS battery scheduling 모니터링을 요청합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ESS battery scheduling 모니터링 요청 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EssSchFrontResponseDto.class))
            )
    })
    @GetMapping
    public void monitorEss() {

        Thread essSchThread = new Thread(new EssSchThread(buildingRepository, memberRepository, essRepository));
        essSchThread.start();
    }
}
