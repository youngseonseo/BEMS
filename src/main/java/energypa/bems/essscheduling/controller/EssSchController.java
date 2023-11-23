package energypa.bems.essscheduling.controller;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.essscheduling.dto.front.EssSchFrontResponseDto;
import energypa.bems.essscheduling.dto.front.Graph1;
import energypa.bems.essscheduling.dto.front.Graph2;
import energypa.bems.essscheduling.dto.front.Graph3;
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

//    private final EssSchService essSchService;
    private final MemberRepository memberRepository;

    @Operation(summary = "ESS battery scheduling 모니터링 요청", description = "유저가 ESS battery scheduling 모니터링을 요청합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "ESS battery scheduling 모니터링 요청 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EssSchFrontResponseDto.class))
            )
    })
    @GetMapping
    public void monitorEss() throws InterruptedException, IOException {
//        essSchService.workWithAIServer();

        for (Member member : memberRepository.findAll()) {

            if (sseEmitters.containsKey(member.getId())) {

                SseEmitter sseEmitter = sseEmitters.get(member.getId());
                if (sseEmitter == null) {
                    sseEmitter  = new SseEmitter();
                    sseEmitters.put(member.getId(), sseEmitter);
                }

                EssSchFrontResponseDto jsonToBeSent = createJsonToBeSent();

                while (true) {
                    System.out.println("jsonToBeSent: " + jsonToBeSent);
                    sseEmitter.send(SseEmitter.event()
                            .name("essBatteryScheduling")
                            .data(jsonToBeSent));

                    Thread.sleep(1000*5);
                }
            }
        }
    }

    private EssSchFrontResponseDto createJsonToBeSent() {

        String timestamp = "2022-08-27 12:02:00";
        Double batteryPower =  -0.05;
        Integer consumption = 140;
        Double tou = 132.2;
        Double netLoad = 2.318482520222584;
        Double threshold = 2.7;
        Double soc = 49.99865;

        Graph1 graph1 = Graph1.builder()
                .timestamp(timestamp)
                .batteryPower(batteryPower)
                .consumption(consumption)
                .tou(tou)
                .build();

        Graph2 graph2 = Graph2.builder()
                .timestamp(timestamp)
                .consumption(consumption)
                .netLoad(netLoad)
                .threshold(threshold)
                .build();

        Graph3 graph3 = Graph3.builder()
                .timestamp(timestamp)
                .soc(soc)
                .build();

        return EssSchFrontResponseDto.builder()
                .graph1(graph1)
                .graph2(graph2)
                .graph3(graph3)
                .build();
    }
}
