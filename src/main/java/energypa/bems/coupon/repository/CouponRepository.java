package energypa.bems.coupon.repository;

import energypa.bems.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query("select cp from Coupon cp where cp.receiver.id = :memberId and cp.dDay >=0 and cp.usedYn = false")
    List<Coupon> getCoupons(@Param("memberId") long memberId);

    @Query(value = "select mem.member_id, mem.building, mem.floor " +
            "from (select member_id from Coupon where d_day >=0 and used_yn = 0 group by member_id) as memWithValidCoupon inner join Member as mem " +
            "on memWithValidCoupon.member_id = mem.member_id",
            nativeQuery = true)
    List<Object[]> getUserWithCoupon();

    @Modifying
    @Query("update Coupon cp set cp.usedYn = true where cp.couponId = :couponId")
    void updateUsedYnOfCoupon(@Param("couponId") long couponId);
}