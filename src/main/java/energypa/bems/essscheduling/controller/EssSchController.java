package energypa.bems.essscheduling.controller;

import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.essscheduling.dto.front.EssSchFrontPrevResponseDto;
import energypa.bems.essscheduling.dto.front.EssSchFrontResponseDto;
import energypa.bems.essscheduling.service.EssSchService;
import energypa.bems.essscheduling.thread.EssSchThread;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Tag(name = "ESS battery scheduling 모니터링", description = "ESS battery scheduling API")
@RestController
@RequestMapping("/api/ess")
@RequiredArgsConstructor
public class EssSchController {

    private final BuildingPerMinuteRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final EssPredictResultRepository essRepository;
    private final EssSchService essService;

    // 모니터링 시작 시점과 종료 시점 == 애플리케이션 실행 시작 시점 종료 시점
    @PostConstruct
    public void init() {

        Thread essSchThread = new Thread(new EssSchThread(buildingRepository, essRepository, essService));
        essSchThread.start();
    }

    @Operation(summary = "ESS battery scheduling 모니터링 요청", description = "유저가 ESS battery scheduling 모니터링을 요청합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ESS battery scheduling 모니터링 요청 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EssSchFrontPrevResponseDto.class))
            )
    })
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> monitorEss(@CurrentUser UserPrincipal userPrincipal) {
//    public ResponseEntity<SseEmitter> monitorEss() {

        Member member = memberRepository.findById(userPrincipal.getId()).get();
        long memberId = member.getId();
//        long memberId = 2l;

        SseEmitter sseEmitter = EssSchThread.sseEmitters.get(memberId);
        if (sseEmitter == null) {
            sseEmitter = new SseEmitter(60*60*1000l);
            EssSchThread.sseEmitters.put(memberId, sseEmitter);
        }

        EssSchFrontPrevResponseDto essSchPrevData = essService.getEssSchPrevData();
        log.info("[ESS prevData] " + essSchPrevData); //
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("getEssSchPrevData")
                    .data(essSchPrevData));
        }
        catch (IOException E) {
            log.info("SSE 연결에 오류가 발생해 ESS 배터리 스케줄링 데이터 전송에 실패하였습니다!");
            // 애플리케이션 관리자에게 알림 주는 코드
        }

        if (essSchPrevData.getGraph1().size() < EssSchService.ESS_PREV_DATA_CNT) {
            EssSchThread.isRunning = false;
            log.info("전체 데이터 조회를 완료했습니다. ESS battery scheduling 모니터링을 종료합니다!");
        }

        return ResponseEntity.ok(sseEmitter);
    }
}
