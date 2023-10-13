package energypa.bems.notification.controller;

import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.payload.response.ApiResponse;
import energypa.bems.login.payload.response.Message;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.notification.domain.Notification;
import energypa.bems.notification.dto.ReceivedNotificationDto;
import energypa.bems.notification.dto.SendNotificationDto;
import energypa.bems.notification.repository.NotificationRepository;
import energypa.bems.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@Tag(name="notification", description = "알림 기능")
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    public static Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    public static final long TIMEOUT = 60L * 1000;

    @Operation(method = "get", summary = "새로운 알람을 확인하는 API")
    @GetMapping("/notifications/new")
    public ResponseEntity<?> getUnreadNotifications(@CurrentUser UserPrincipal user) {
        Member member= memberRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보가 없습니다."));
        List<Notification> notifications = notificationRepository.findByMemberAndCheckedOrderByCreatedDesc(member, false);
        long numberOfChecked = notificationRepository.countByMemberAndChecked(member, true);
        notificationService.markAsRead(notifications);    // 확인한 알람으로 보내기

        return ResponseEntity.ok(new ReceivedNotificationDto(numberOfChecked, notifications));
    }


    @Operation(method = "get", summary = "읽었던 알람을 확인하는 API")
    @GetMapping("/notifications/old")
    public ResponseEntity<?> getReadedNotifications(@CurrentUser UserPrincipal user) {
        Member member= memberRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보가 없습니다."));
        List<Notification> notifications = notificationRepository.findByMemberAndCheckedOrderByCreatedDesc(member, true);
        Long numberOfNotChecked = notificationRepository.countByMemberAndChecked(member, false);

        return ResponseEntity.ok(new ReceivedNotificationDto(numberOfNotChecked, notifications));
    }


    @Operation(method = "delete", description = "알림 모두 삭제하는 API")
    @DeleteMapping("/notifications")
    public ResponseEntity<?> deleteNotifications(@CurrentUser UserPrincipal user) {
        Member member= memberRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보가 없습니다."));
        notificationRepository.deleteByMemberAndChecked(member, true);
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("알림을 모두 삭제했습니다.").build()).build();
        return ResponseEntity.ok(apiResponse);
    }


    @Operation(method = "post", description = "클라이언트 개발을 위한 notification 추가하는 APIi")
    @PostMapping("/notification")
    public ResponseEntity addNotification(
            @CurrentUser UserPrincipal user,
            @RequestBody SendNotificationDto sendNotificationDto) {

        Member member= memberRepository.findById(user.getId()).orElseThrow(() ->
                new IllegalArgumentException("유저 정보가 없습니다."));

        Notification notification = notificationService.addNotification(sendNotificationDto);

        // 알림 이벤트 발행 메서드 호출
        notificationService.notifyAddEvent(notification.getId());

        return ResponseEntity.ok("ok");
    }


    @Operation(method="get", description = "SSE 알림 기능 구독하는 API")
    @CrossOrigin
    @GetMapping(value = "/sub", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@CurrentUser UserPrincipal user) {

        // 토큰에서 user의 pk값 파싱
        Long userId = user.getId();

        // 현재 클라이언트를 위한 SseEmitter 생성
        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);
        try {
            // 연결!!
            sseEmitter.send(SseEmitter.event().name("connect"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // user의 pk값을 key값으로 해서 SseEmitter를 저장
        sseEmitters.put(userId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));
        sseEmitter.onError((e) -> sseEmitters.remove(userId));

        return sseEmitter;
    }

}