package energypa.bems.coupon.service;

import energypa.bems.coupon.dto.UserWithCouponDto;
import energypa.bems.coupon.entity.Coupon;
import energypa.bems.coupon.repository.CouponRepository;
import energypa.bems.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getCoupons(Member member) {
        return couponRepository.getCoupons(member.getId());
    }

    public List<UserWithCouponDto> getUserWithCoupon() {

        return couponRepository.getUserWithCoupon().stream()
                .map(row -> new UserWithCouponDto((Long) row[0],(Integer) row[1], (Integer) row[2]))
                .collect(Collectors.toList());
    }
}
