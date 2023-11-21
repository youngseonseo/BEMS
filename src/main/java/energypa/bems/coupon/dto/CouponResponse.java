package energypa.bems.coupon.dto;

import energypa.bems.coupon.entity.Coupon;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(description = "쿠폰 info")
@Data
@Builder
public class CouponResponse {

    @Schema(description = "유저가 보유한 쿠폰")
    private List<Coupon> myCoupons;

    @Schema(description = "유저가 보유한 쿠폰 개수", example = "5")
    private Integer numOfCoupons;
}