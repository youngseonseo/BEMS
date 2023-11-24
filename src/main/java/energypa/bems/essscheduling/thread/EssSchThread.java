package energypa.bems.essscheduling.thread;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.domain.EssPredictResult;
import energypa.bems.essscheduling.dto.ai.EssSchAiRequestDto;
import energypa.bems.essscheduling.dto.ai.EssSchAiResponseDto;
import energypa.bems.essscheduling.dto.front.EssSchFrontResponseDto;
import energypa.bems.essscheduling.dto.front.Graph1;
import energypa.bems.essscheduling.dto.front.Graph2;
import energypa.bems.essscheduling.dto.front.Graph3;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Optional;

import static energypa.bems.notification.controller.NotificationController.sseEmitters;

@Slf4j
@RequiredArgsConstructor
public class EssSchThread implements Runnable {

    private final BuildingPerMinuteRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final EssPredictResultRepository essRepository;

    private long buildingPerMinuteId = 1l;
    private boolean isRunning = true;
    private EssSchAiResponseDto essResponseDto = null;

    @Override
    public void run() {

        while(isRunning) {

            try {
                // DB 조회 (DB 데이터가 존재하지 않는 경우 모니터링 종료)
                BuildingPerMinute bdConsumption = getBdConsumptionFromDb();

                // AI 서버에 요청 보내고 응답받기
//                essResponseDto = workWithAIServer(essResponseDto, bdConsumption);
                EssPredictResult aiDataFromDb = getAiDataFromDb();

                // SSE 연결 통해서 클라이언트에게 json 전달
                sendJsonToClient(bdConsumption, aiDataFromDb);

                // 쓰레드 휴식
                Thread.sleep(1000*5);
            }
            catch (IllegalStateException e) {
                isRunning = false;
                log.info("BuildingPerMinute 또는 EssPredictResult 전체 데이터 조회 완료! ESS battery scheduling 모니터링을 종료합니다.", e);
            }
//            catch (URISyntaxException e) {
//                log.error("AI 서버 요청 URI가 유효하지 않습니다!", e);
//            }
            catch (IOException e) {
                log.error("클라이언트와 백엔드 서버 간의 SSE 연결이 종료되어 JSON을 전달할 수 없습니다!", e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private BuildingPerMinute getBdConsumptionFromDb() throws IllegalStateException {

        Optional<BuildingPerMinute> bdConsumptionOp = buildingRepository.findById(buildingPerMinuteId);

        // DB(BuildingPerMinute) 데이터를 모두 조회한 경우 - ESS battery scheduling 모니터링 종료
        if (bdConsumptionOp.isEmpty()) {
            throw new IllegalStateException("BuildingPerMinute에서 전체 데이터 조회를 완료했습니다. ESS battery scheduling 모니터링을 종료합니다!");
        }

        return bdConsumptionOp.get();
    }

    private EssPredictResult getAiDataFromDb() throws IllegalStateException {

        Optional<EssPredictResult> essAiResOp = essRepository.getAiResFromDb(buildingPerMinuteId++);

        // DB(EssPredictResult) 데이터를 모두 조회한 경우 - ESS battery scheduling 모니터링 종료
        if (essAiResOp.isEmpty()) {
            throw new IllegalStateException("EssPredictResult에서 전체 데이터 조회를 완료했습니다. ESS battery scheduling 모니터링을 종료합니다!");
        }

        return essAiResOp.get();
    }

//    private EssSchAiResponseDto workWithAIServer(EssSchAiResponseDto essSchResponseDto, BuildingPerMinute bdConsumption) throws URISyntaxException {
//
//        // 요청 보낼 ai 서버의 URL
//        URI url = new URI("http://127.0.0.1:10000/ess/optimal");
//
//        // 전달할 json 데이터
//        double soc = 50.0;
//        double batteryPower = 0.0;
//        if (buildingPerMinuteId++ > 1l) {
//            soc = essSchResponseDto.getSoc();
//            batteryPower = essSchResponseDto.getBatteryPower();
//        }
//
//        EssSchAiRequestDto essRequestDto = EssSchAiRequestDto.builder()
//                .timestamp(bdConsumption.getTimestamp().toString())
//                .soc(soc)
//                .batteryPower(batteryPower)
//                .consumptionOf561(bdConsumption.getA_Consumption())
//                .consumptionOf562(bdConsumption.getB_Consumption())
//                .consumptionOf563(bdConsumption.getC_Consumption())
//                .build();
//        log.info("[AI 요청] " + essRequestDto); //
//
//        // RestTemplate 통해서 ai 서버에 요청 보내고 응답받기
//
//        // method: post
//        // url: http://127.0.0.1:10000/ess/optimal
//        // header: Content-Type APPLICATION_JSON
//        // body: EssSchRequestDto
//
//        RequestEntity<EssSchAiRequestDto> essRequestEntity = RequestEntity.post(url)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(essRequestDto);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<EssSchAiResponseDto> respEntity = restTemplate.exchange(essRequestEntity, EssSchAiResponseDto.class);
//
//        EssSchAiResponseDto essResponseDto = respEntity.getBody();
//        log.info("[AI 응답] " + essResponseDto); //
//        return essResponseDto;
//    }

    private void sendJsonToClient(BuildingPerMinute bdConsumption, EssPredictResult aiDataFromDb) throws IOException {

        EssSchFrontResponseDto essFrontResponse = createJsonToBeSent(bdConsumption, aiDataFromDb);
        log.info("[front 응답] " + essFrontResponse); //

        for (Member member : memberRepository.findAll()) {

            if (sseEmitters.containsKey(member.getId())) {

                SseEmitter sseEmitter = sseEmitters.get(member.getId());
                if (sseEmitter == null) {
                    sseEmitter  = new SseEmitter();
                    sseEmitters.put(member.getId(), sseEmitter);
                }

                sseEmitter.send(SseEmitter.event()
                        .name("essBatteryScheduling")
                        .data(essFrontResponse));
            }
        }
    }

    private EssSchFrontResponseDto createJsonToBeSent(BuildingPerMinute bdConsumption, EssPredictResult aiDataFromDb) {

        int consumption = sumConsumption(bdConsumption);

        String timestamp = aiDataFromDb.getTimestamp().toString();
        Double batteryPower = aiDataFromDb.getBatteryPower();
        Double soc = aiDataFromDb.getSoc();
        Double tou = aiDataFromDb.getTouRate();
        Double netLoad = aiDataFromDb.getNetLoad();
        Double threshold = aiDataFromDb.getThreshold();

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

//    private EssSchFrontResponseDto createJsonToBeSent(BuildingPerMinute bdConsumption) {
//
//        String timestamp = getTimestamp();
//        int consumption = sumConsumption(bdConsumption);
//
//        Graph1 graph1 = Graph1.builder()
//                .timestamp(timestamp)
//                .batteryPower(essResponseDto.getBatteryPower())
//                .consumption(consumption)
//                .tou(essResponseDto.getTou())
//                .build();
//
//        Graph2 graph2 = Graph2.builder()
//                .timestamp(timestamp)
//                .consumption(consumption)
//                .netLoad(essResponseDto.getNetLoad())
//                .threshold(essResponseDto.getThreshold())
//                .build();
//
//        Graph3 graph3 = Graph3.builder()
//                .timestamp(timestamp)
//                .soc(essResponseDto.getSoc())
//                .build();
//
//        return EssSchFrontResponseDto.builder()
//                .graph1(graph1)
//                .graph2(graph2)
//                .graph3(graph3)
//                .build();
//    }

//    private String getTimestamp() {
//        return essResponseDto.getTimestamp();
//    }

    private int sumConsumption(BuildingPerMinute bdConsumption) {
        return (int) (bdConsumption.getA_Consumption() + bdConsumption.getB_Consumption() + bdConsumption.getC_Consumption());
    }
}