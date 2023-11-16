package energypa.bems.coupon.service;

import energypa.bems.coupon.entity.Coupon;
import energypa.bems.coupon.repository.CouponRepository;
import energypa.bems.login.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<Coupon> getCoupons(Member member) {
        return couponRepository.findByReceiverAndUsedYn(member, false);
    }

    public int getNumOfCoupons(Member member) {
        return couponRepository.getNumOfCoupons(member,false);
    }
}
