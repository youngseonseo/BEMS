package energypa.bems.monitoring.controller;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.monitoring.dto.MonitorBuildingResponse;
import energypa.bems.monitoring.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Tag(name = "monitor", description = "모니터링 API")
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitoringController {

    private final MonitoringService monitoringService;

    @Operation(summary = "층 별 모니터링 요청", description = "층 별 전력 사용량 및 예측량 모니터링 요청")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "모니터링 성공",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BuildingPerTenMinute.class)))
            )
    })
    @ResponseBody
    @GetMapping(value = "/floor", produces = "text/event-stream")
    public SseEmitter monitorByFloor(
            @RequestParam(value = "building") Integer building,
            @RequestParam(value = "floor") Integer floor,
            @CurrentUser UserPrincipal userPrincipal)
    {

        // 클라이언트-서버 간 SSE 연결
        SseEmitter sseEmitter = monitoringService.formSseConnectionForFloor(building, floor, userPrincipal);

        // prev monitoring floor info 전송
        List<BuildingPerTenMinute> prevFloorInfo = monitoringService.getPrevFloorInfo(building, floor);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sendPrevFloorInfo")
                    .data(prevFloorInfo));
        } catch (IOException e) {
            log.error("", e);
        }

        // 10분 주기로 데이터를 서버 -> 클라이언트 전송

        return sseEmitter;
    }

    @Operation(summary = "동 별 모니터링 요청", description = "동 별 전력 사용량 및 예측량 모니터링 요청")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "모니터링 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MonitorBuildingResponse.class))
            )
    })
    @ResponseBody
    @GetMapping(value = "/building", produces = "text/event-stream")
    public SseEmitter monitorByBuilding(
            @RequestParam("duration") String duration,
            @CurrentUser UserPrincipal userPrincipal)
    {
        // 클라이언트-서버 간 SSE 연결
        SseEmitter sseEmitter = monitoringService.formSseConnectionForBuilding(userPrincipal);

        // prev monitoring building info 전송
        MonitorBuildingResponse prevBuildingInfo = monitoringService.getPrevBuildingInfo(duration);
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("sendPrevBuildingInfo")
                    .data(prevBuildingInfo));
        } catch (IOException e) {
            log.error("", e);
        }

        // 1분 주기로 데이터를 서버 -> 클라이언트 전송

        return sseEmitter;
    }
}
