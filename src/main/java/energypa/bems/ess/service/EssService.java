package energypa.bems.ess.service;

import energypa.bems.energy.repository.EssBatteryRepository;
import energypa.bems.ess.dto.BusDto;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.sql.Timestamp;
import java.util.List;

import static energypa.bems.notification.controller.NotificationController.sseEmitters;

@Service
@RequiredArgsConstructor
public class EssService {

    private final EssBatteryRepository essBatteryRepository;
    private final MemberRepository memberRepository;

    public void essMonitorService() {

        try {

            Timestamp now = new Timestamp(System.currentTimeMillis());
            Timestamp startDt = new Timestamp(now.getYear() - 1, now.getMonth() - 4, now.getDate() + 15, now.getHours(), now.getMinutes(), now.getSeconds(), now.getNanos());
            Timestamp endDt = new Timestamp(now.getYear()-1,now.getMonth()-3,now.getDate()+15, now.getHours(),now.getMinutes(),now.getSeconds(), now.getNanos());

            List<BusDto> aBus = essBatteryRepository.findA_bus(startDt, endDt);

            for (BusDto bus : aBus) {
                List<Member> all = memberRepository.findAll();
                for (Member member : all) {
                    if (sseEmitters.containsKey(member.getId())) {
                        SseEmitter sseEmitter = sseEmitters.get(member.getId());
                        if(sseEmitter!=null) {
                            try {
                                sseEmitter.send(SseEmitter.event().name("ess").data(bus));
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                sseEmitters.remove(member.getId());
                            }
                        }
                        else
                            return;
                    }
                }
            }

        } catch (Exception e) {
            sseEmitters.remove(1L/**userId*/);
        }
    }
}
