package energypa.bems.coupon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "쿠폰 보유한 유저")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithCouponDto {

    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "유저 이름", example = "유저1")
    private String username;

    @Schema(description = "동", example = "561")
    private Integer building;

    @Schema(description = "층", example = "1")
    private Integer floor;
}
