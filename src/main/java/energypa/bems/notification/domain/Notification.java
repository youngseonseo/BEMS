package energypa.bems.notification.domain;

import energypa.bems.login.domain.Member;
import energypa.bems.notification.dto.SendNotificationDto;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String title;


    private String message;

    private boolean checked;

    @ManyToOne
    private Member member;

    private LocalDateTime created;


    public static Notification toEntity(SendNotificationDto dto) {
        return Notification.builder()
                .title(dto.getTitle())
                .message(dto.getMessage())
                .checked(dto.getChecked())
                .member(dto.getMember())
                .created(dto.getCreated())
                .build();
    }

    public void read() {
        this.checked = true;
    }
}