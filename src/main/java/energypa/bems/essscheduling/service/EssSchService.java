package energypa.bems.essscheduling.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.essscheduling.dto.EssSchRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class EssSchService {

    private final ObjectMapper objectMapper;

    public void workWithAIServer() {

        // 요청 보낼 ai 서버의 URL
        String url = "http://127.0.0.1:10000/ess/optimal";

        // 전달할 json 데이터
        EssSchRequestDto essRequestDto = EssSchRequestDto.builder()
                .timestamp(LocalDateTime.now())
                .soc(50.0)
                .batteryPower(0.0)
                .consumptionOf561(62.1733)
                .consumptionOf562(62.1733)
                .consumptionOf563(62.1733)
                .build();

        String essRequestJson = null;
        try {
            essRequestJson = objectMapper.writeValueAsString(essRequestDto);
        }
        catch (JsonProcessingException e) {
            log.error("EssSchRequestDto 객체를 JSON으로 변환하는데 실패했습니다!", e);
        }

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
