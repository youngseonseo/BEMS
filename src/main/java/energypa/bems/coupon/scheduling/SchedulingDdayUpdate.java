package energypa.bems.coupon.scheduling;

import energypa.bems.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulingDdayUpdate {

    private final CouponRepository couponRepository;

    // 매일 자정(00:00:00)에 동작하는 스케줄러
    // 목적: 쿠폰의 d-day 업데이트
    // 대상: d-day 업데이트가 의미있는 쿠폰 - 사용 기한 내에 있으면서 아직 사용하지 않은 쿠폰
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateDdayOfCoupon() {

        couponRepository.updateDdayOfCoupons();
    }
}
