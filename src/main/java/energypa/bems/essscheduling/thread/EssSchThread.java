package energypa.bems.essscheduling.thread;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.essscheduling.dto.EssSchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EssSchThread implements Runnable {

    private final BuildingPerMinuteRepository buildingRepository;
    private final ObjectMapper objectMapper;

    private long buildingPerMinuteId = 1l;
    private boolean isRunning = true;

    @Override
    public void run() {

        while(isRunning) {

            try {
                // DB 조회 (DB 데이터가 존재하지 않는 경우 모니터링 종료)
                BuildingPerMinute bdConsumption = getBdConsumptionFromDb();

                // AI 서버에 요청 보내고 응답받기
                workWithAIServer(bdConsumption);

                // 클라이언트에게 json 전달


                // 쓰레드 휴식
                Thread.sleep(1000*5);
            }
            catch (IllegalStateException e) {
                isRunning = false;
                log.info("BuildingPerMinute 전체 데이터 조회 완료! ESS battery scheduling 모니터링을 종료합니다.");
            }
            catch (JsonProcessingException e) {
                log.error("EssSchRequestDto 객체를 JSON으로 변환하는데 실패했습니다!", e);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private BuildingPerMinute getBdConsumptionFromDb() throws IllegalStateException {

        Optional<BuildingPerMinute> bdConsumptionOp = buildingRepository.findById(buildingPerMinuteId++);

        // DB(BuildingPerMinute) 데이터를 모두 조회한 경우 - ESS battery scheduling 모니터링 종료
        if (bdConsumptionOp.isEmpty()) {
            throw new IllegalStateException("전체 데이터 조회를 완료했습니다. ESS battery scheduling 모니터링을 종료합니다!");
        }

        return bdConsumptionOp.get();
    }

    private void workWithAIServer(BuildingPerMinute bdConsumption) throws JsonProcessingException {

        // 요청 보낼 ai 서버의 URL
        String url = "http://127.0.0.1:10000/ess/optimal";

        // 전달할 json 데이터
        EssSchRequestDto essRequestDto = EssSchRequestDto.builder()
                .timestamp(bdConsumption.getTimestamp())
                .soc(50.0)
                .batteryPower(0.0)
                .consumptionOf561(bdConsumption.getA_Consumption())
                .consumptionOf562(bdConsumption.getB_Consumption())
                .consumptionOf563(bdConsumption.getC_Consumption())
                .build();

        String essRequestJson = objectMapper.writeValueAsString(essRequestDto);

        // WebClient 통해서 ai 서버에 요청 보내고 응답받기
        WebClient.create()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(essRequestJson))
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(essResponseBody -> {
                    log.info("Response from AI Server: " + essResponseBody);
                });
    }
}