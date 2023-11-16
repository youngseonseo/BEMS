package energypa.bems.coupon.entity;

import energypa.bems.login.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "쿠폰 info")
@Data
@Entity
public class Coupon {

    @Schema(description = "쿠폰 고유 번호", example = "1")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Schema(description = "쿠폰 수신자", example = "1")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member receiver;

    @Schema(description = "쿠폰 종류", example = "P5")
    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_type")
    private CouponType couponType;

    @Schema(description = "쿠폰 발급일", example = "")
    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Schema(description = "쿠폰 만료일", example = "")
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Schema(description = "남은 쿠폰 사용 가능 일자", example = "100")
    @Column(name = "d_day")
    private Integer dDay;

    @Schema(description = "쿠폰 사용 여부", example = "true")
    @Column(name = "used_yn")
    private Boolean usedYn;
}
