package energypa.bems.notification.service;

import energypa.bems.energy.repository.TotalOneHourRepository;
import energypa.bems.monitoring.service.MonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
@Service
public class UsageNotificationService {


    private final NotificationService notificationService;
    private final TotalOneHourRepository totalOneHourRepository;
    private final MonitoringService monitoringService;

    int check50 = 0;
    int check70 = 0;
    int check100 = 0;

    public String getYesterday() {
        LocalDateTime todayDate = monitoringService.manipulateNowForBuilding();
        return todayDate.toLocalDate().minusDays(1L).toString();
    }
    @Scheduled(cron = "0 * * * * ?")
//    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")
    public void usageAlertService(){

        Double yesterdayConsumption = totalOneHourRepository.getYesterdayConsumption(getYesterday());
        Timestamp now = Timestamp.valueOf(monitoringService.manipulateNowForBuilding());
        Timestamp start = Timestamp.valueOf(monitoringService.manipulateNowForBuilding().withHour(0));  // 오늘 00시
        Double usage = totalOneHourRepository.getFromNowConsumption(start, now);
        System.out.println("yesterdayConsumption = " + yesterdayConsumption);
        System.out.println("usage = " + usage);

        if (check50 ==0 && usage > yesterdayConsumption * 0.5) {
            notificationService.serverSendNotification(50);
            check50 = 1;   // 알림 보냄을 체크
        }
        if (check70 ==0 && usage > yesterdayConsumption * 0.7) {
            notificationService.serverSendNotification(70);
            check70 = 1;
        }
        if (check100 ==0 && usage > yesterdayConsumption) {
            notificationService.serverSendNotification(100);
            check100 = 1;
        }
    }


    @Scheduled(cron = "0 0 0 1 * ?")
    public void initUsageAlertService(){     // 매일 자정에 check 초기화
        check50 = 0;
        check70 = 0;
        check100 = 0;
    }
}
