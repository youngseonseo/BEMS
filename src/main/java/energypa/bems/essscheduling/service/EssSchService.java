package energypa.bems.essscheduling.service;

import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.essscheduling.thread.EssSchThread;
import energypa.bems.login.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EssSchService {

    private final BuildingPerMinuteRepository buildingRepository;
    private final MemberRepository memberRepository;
    private final EssPredictResultRepository essRepository;

    // 모니터링 시작 시점과 종료 시점 == 애플리케이션 실행 시작 시점 종료 시점
    @PostConstruct
    public void init() {

        Thread essSchThread = new Thread(new EssSchThread(buildingRepository, memberRepository, essRepository));
//        essSchThread.start();
    }
}
