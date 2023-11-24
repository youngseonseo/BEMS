package energypa.bems.coupon.scheduling;

import energypa.bems.coupon.dto.PowerUsageDto;
import energypa.bems.coupon.entity.Coupon;
import energypa.bems.coupon.entity.CouponType;
import energypa.bems.coupon.repository.CouponRepository;
import energypa.bems.energy.repository.BuildingPerTenMinuteRepository;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulingCouponIssuance {

    private final BuildingPerTenMinuteRepository floorRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;

    private final int numOfBuilding = 3;
    private final int[] buildingList = { 561, 562, 563 };
    private final int[] highestFloorNumList = { 18, 24, 24 };

    // 매달 1일 00시 00분 00초에 동작하는 스케줄러
    // 목적: 전력 사용량 조건 만족하는 모든 유저에게 쿠폰 발급
    @Scheduled(cron = "0 0 0 1 * ?")
    public void issueCoupons() {

        for(int i=0; i<numOfBuilding; i++) {

            int highestBuildingNum = highestFloorNumList[i];

            for(int j=1; j<=highestBuildingNum; j++) {

                int building = buildingList[i];
                int floor = j;

                try {
                    int powerUsage1MonthsAgo = getPowerUsageForASpecificMonth(building, floor, getDate1MonthAgo().getYear(), getDate1MonthAgo().getMonthValue());
                    int powerUsage2MonthsAgo = getPowerUsageForASpecificMonth(building, floor, getDate2MonthAgo().getYear(), getDate2MonthAgo().getMonthValue());
                    double rateOfIncrease = getRateOfIncrease(powerUsage2MonthsAgo, powerUsage1MonthsAgo);

                    issueACoupon(building, floor, rateOfIncrease);

                } catch (IllegalStateException e) {
                    log.info(building + "동 " + floor + "층은 존재하지 않습니다!");
                } catch (NullPointerException e) {
                    log.info("이전 전력 사용량 데이터가 존재하지 않습니다. 쿠폰 발급이 불가한 조건입니다!");
                }
            }
        }
    }

    private void issueACoupon(int building, int floor, double rateOfIncrease) {

        if (rateOfIncrease <= 0.95) {

            log.info("P5 쿠폰 발급 대상!");

            Member member = getMember(building, floor);
            LocalDate issueDate = getIssueDate();
            LocalDate expirationDate = getExpirationDate(issueDate);
            int dDay = getDDay(issueDate, expirationDate);

            Coupon p5 = Coupon.builder()
                    .receiver(member)
                    .couponType(CouponType.P5)
                    .issueDate(issueDate)
                    .expirationDate(expirationDate)
                    .dDay(dDay)
                    .usedYn(false)
                    .build();
            couponRepository.save(p5);
        }
        else if (rateOfIncrease <= 0.97) {

            log.info("P3 쿠폰 발급 대상!");

            Member member = memberRepository.findByBuildingAndFloor(building, floor);
            LocalDate issueDate = getIssueDate();
            LocalDate expirationDate = getExpirationDate(issueDate);
            int dDay = getDDay(issueDate, expirationDate);

            Coupon p3 = Coupon.builder()
                    .receiver(member)
                    .couponType(CouponType.P3)
                    .issueDate(issueDate)
                    .expirationDate(expirationDate)
                    .dDay(dDay)
                    .usedYn(false)
                    .build();
            couponRepository.save(p3);
        }
    }

    private LocalDate manipulateCurrentDate() {

        // [temporary] current date를 2023년 8월로 조작!
        return LocalDate.now().minusYears(0l).minusMonths(3l);
    }

    private LocalDate getDate1MonthAgo() {
        return manipulateCurrentDate().minusMonths(1l);
    }

    private LocalDate getDate2MonthAgo() {
        return manipulateCurrentDate().minusMonths(2l);
    }

    private Member getMember(int myBuilding, int myFloor) {
        return memberRepository.findByBuildingAndFloor(myBuilding, myFloor);
    }

    private LocalDate getIssueDate() {
        return manipulateCurrentDate();
    }

    private LocalDate getExpirationDate(LocalDate issueDate) {
        return issueDate.plusMonths(6l).minusDays(1l);
    }

    private int getDDay(LocalDate issueDate, LocalDate expirationDate) {
        return (int) ChronoUnit.DAYS.between(issueDate, expirationDate);
    }

    private double getRateOfIncrease(int powerUsage2MonthsAgo, int powerUsage1MonthsAgo) {
        return powerUsage1MonthsAgo / powerUsage2MonthsAgo;
    }

    private int getPowerUsageForASpecificMonth(int building, int floor, int year, int month) {

        int existenceOfThatFloor = floorRepository.getExistenceOfThatFloor(building, floor);
        System.out.println("existenceOfThatFloor: " + existenceOfThatFloor);
        if (existenceOfThatFloor == 0) {
            throw new IllegalStateException();
        }

        int powerUsage = floorRepository.getPowerUsageForASpecificMonth(building, floor, year, month).stream()
                .map(row -> new PowerUsageDto((Integer) row[0], (Integer) row[1], ((BigDecimal)row[2]).intValue()))
                .collect(Collectors.toList())
                .get(0)
                .getTotalConsumption();

        log.info(year + "년 " + month + "월의 전력사용량: " + powerUsage);

        return powerUsage;
    }
}