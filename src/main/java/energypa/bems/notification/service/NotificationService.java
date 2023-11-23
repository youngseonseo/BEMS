package energypa.bems.notification.service;



import energypa.bems.login.domain.Authority;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.notification.domain.Notification;
import energypa.bems.notification.dto.SendNotificationDto;
import energypa.bems.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static energypa.bems.notification.controller.NotificationController.sseEmitters;


@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    public void notifyAddEvent(Long notificationId) {
        // 댓글에 대한 처리 후 해당 댓글이 달린 게시글의 pk값으로 게시글을 조회
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(
                () -> new IllegalArgumentException("찾을 수 없는 알림입니다.")
        );
        Long userId = notification.getMember().getId();

        if (sseEmitters.containsKey(userId)) {
            SseEmitter sseEmitter = sseEmitters.get(userId);
            try {
                sseEmitter.send(SseEmitter.event().name("addNotification").data("알림이 추가되었습니다."));
            } catch (Exception e) {
                sseEmitters.remove(userId);
            }
        }
    }

    public void markAsRead(List<Notification> notifications) {
        for (Notification notification : notifications) {
            notificationRepository.updateChecked(notification.getId());
        }
    }

    public Notification addNotification(SendNotificationDto sendNotificationDto, Member member) {
        Notification notification = Notification.toEntity(sendNotificationDto, member);
        notificationRepository.save(notification);

        return notification;
    }

    public void serverSendNotification(Integer num) {    // 서버용 알림 보내기
        String title = String.format("%d%% 전력 사용", num);
        String message = String.format("전일 대비 %d%% 전력을 사용하였습니다. 알림을 확인해보세요", num);
        SendNotificationDto sendNotificationDto = new SendNotificationDto(title, message);
        List<Member> members = memberRepository.findAllByAuthority(Authority.MANAGER);
        System.out.println("members = " + members);
        for (Member member : members) {
            Notification notification = addNotification(sendNotificationDto, member);
            // 알림 이벤트 발행 메서드 호출
            notifyAddEvent(notification.getId());
        }
    }
}