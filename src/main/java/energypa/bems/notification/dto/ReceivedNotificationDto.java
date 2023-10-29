package energypa.bems.notification.dto;


import energypa.bems.notification.domain.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReceivedNotificationDto {

    private Long numberOfNotification;
    private List<Notification> notifications;

}
