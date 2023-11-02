package energypa.bems.monitoring.repository;

import energypa.bems.login.domain.Member;
import energypa.bems.monitoring.dto.FloorInfo;
import lombok.Getter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Repository
public class SseRepository {

    private final Map<SseEmitter, FloorInfo> sseEmitterMap = new ConcurrentHashMap();

    public void save(SseEmitter sseEmitter, int building, int floor) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = ((Member) authentication.getPrincipal());

        FloorInfo myFloorInfo = FloorInfo.builder()
                .authority(member.getAuthority())
                .building(building)
                .floor(floor)
                .build();

        sseEmitterMap.values().stream().forEach(floorInfo -> {
            if (floorInfo.equals(myFloorInfo)) { // 클라이언트가 모니터링 페이지에 재방문한 경우
                return;
            }
        });
        sseEmitterMap.put(sseEmitter, myFloorInfo);

//        sseEmitter.onCompletion(() -> {
//            sseEmitterList.remove(sseEmitter);
//        });

//        sseEmitter.onTimeout(() -> {
//            sseEmitter.complete();
//        });
    }
}