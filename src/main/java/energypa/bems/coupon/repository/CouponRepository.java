package energypa.bems.coupon.repository;

import energypa.bems.coupon.entity.Coupon;
import energypa.bems.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select cp from Coupon cp where cp.receiver.id = :memberId and cp.dDay >=0 and cp.usedYn = false")
    List<Coupon> getCoupons(@Param("memberId") long memberId);

    @Query("select count(cp) from Coupon cp where cp.receiver = :receiver and cp.usedYn = :usedYn")
    int getNumOfCoupons(@Param("receiver") Member receiver, @Param("usedYn") boolean usedYn);
}