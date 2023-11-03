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

    private final Map<SseEmitter, FloorInfo> sseEmitterFloorMap = new ConcurrentHashMap();
    private final Map<Long, SseEmitter> sseEmitterBuildingMap = new ConcurrentHashMap();

    private Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    private Member member = ((Member) authentication.getPrincipal());

    public void saveForFloor(SseEmitter sseEmitter, int building, int floor) {

        FloorInfo myFloorInfo = FloorInfo.builder()
                .authority(member.getAuthority())
                .building(building)
                .floor(floor)
                .build();

        sseEmitterFloorMap.values().stream().forEach(floorInfo -> {
            if (floorInfo.equals(myFloorInfo)) { // 클라이언트가 모니터링 페이지에 재방문한 경우
                return;
            }
        });
        sseEmitterFloorMap.put(sseEmitter, myFloorInfo);

//        sseEmitter.onCompletion(() -> {
//            sseEmitterList.remove(sseEmitter);
//        });

//        sseEmitter.onTimeout(() -> {
//            sseEmitter.complete();
//        });
    }

    public void saveForBuilding(SseEmitter sseEmitter) {

        Long myId = member.getId();

        sseEmitterBuildingMap.keySet().stream().forEach(id -> {
            if (id == myId) { // 클라이언트가 모니터링 페이지에 재방문한 경우
                return;
            }
        });
        sseEmitterBuildingMap.put(myId, sseEmitter);
    }
}