package energypa.bems.notification.dto;


import energypa.bems.login.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class SendNotificationDto {

    private String title;

    private String message;

    private Boolean checked;

    private Member member;

    private LocalDateTime created;


}
