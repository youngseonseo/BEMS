package energypa.bems.essscheduling.thread;

import energypa.bems.energy.domain.BuildingPerMinute;
import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.essscheduling.dto.EssSchRequestDto;
import energypa.bems.essscheduling.dto.EssSchResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class EssSchThread implements Runnable {

    private final BuildingPerMinuteRepository buildingRepository;

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
                log.info("BuildingPerMinute 전체 데이터 조회 완료! ESS battery scheduling 모니터링을 종료합니다.", e);
            }
            catch (URISyntaxException e) {
                log.error("AI 서버 요청 URI가 유효하지 않습니다!", e);
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

    private void workWithAIServer(BuildingPerMinute bdConsumption) throws URISyntaxException {

        // 요청 보낼 ai 서버의 URL
        URI url = new URI("http://127.0.0.1:10000/ess/optimal");

        // 전달할 json 데이터
        EssSchRequestDto essRequestDto = EssSchRequestDto.builder()
                .timestamp(bdConsumption.getTimestamp().toString())
                .soc(50.0)
                .batteryPower(0.0)
                .consumptionOf561(bdConsumption.getA_Consumption())
                .consumptionOf562(bdConsumption.getB_Consumption())
                .consumptionOf563(bdConsumption.getC_Consumption())
                .build();

        // RestTemplate 통해서 ai 서버에 요청 보내고 응답받기

        // method: post
        // url: http://127.0.0.1:10000/ess/optimal
        // header: Content-Type APPLICATION_JSON
        // body: EssSchRequestDto

        RequestEntity<EssSchRequestDto> essRequestEntity = RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(essRequestDto);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EssSchResponseDto> respEntity = restTemplate.exchange(essRequestEntity, EssSchResponseDto.class);

        EssSchResponseDto essResponseDto = respEntity.getBody();
        System.out.println("essResponseDto: " + essResponseDto);
    }
}