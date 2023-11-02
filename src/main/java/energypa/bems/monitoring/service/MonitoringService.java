package energypa.bems.monitoring.service;

import energypa.bems.energy.domain.BuildingPerTenMinute;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.monitoring.repository.SseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitoringService {

    private final BuildingPerTenMinuteRepository floorRepository;
    private final SseRepository sseRepository;

    public SseEmitter formSseConnection(int building, int floor) {

        // sse 연결
        SseEmitter sseEmitter = new SseEmitter();

        // 생성한 SseEmitter 객체 저장
        sseRepository.save(sseEmitter, building, floor);

        // sse 연결 후 dummy data 전송
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect")
                    .data("dummy data to prevent 503"));
        } catch (IOException e) {
            log.error("", e);
        }

        return sseEmitter;
    }

    public LocalDateTime manipulateNow() {

        LocalDateTime now = LocalDateTime.now();
        return now.minusMonths(7L);
    }

    public LocalDateTime manipulateNowWithSetSec0() {

        return manipulateNow().withSecond(0).withNano(0);
    }

    public List<BuildingPerTenMinute> getPrevFloorInfo(int building, int floor) {

        Timestamp now = Timestamp.valueOf(manipulateNow());
        return floorRepository.getPrevFloorConsumption(building, floor, now);
    }
}