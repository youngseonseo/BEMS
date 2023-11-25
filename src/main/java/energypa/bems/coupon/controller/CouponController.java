package energypa.bems.coupon.controller;

import energypa.bems.coupon.dto.CouponResponse;
import energypa.bems.coupon.dto.UserWithCouponDto;
import energypa.bems.coupon.entity.Coupon;
import energypa.bems.coupon.service.CouponService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "coupon", description = "쿠폰 API")
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final MemberRepository memberRepository;

    @Operation(summary = "유저 쿠폰 페이지 요청", description = "유저가 쿠폰 페이지를 요청한 경우, 해당 유저가 보유한 모든 쿠폰을 보여줍니다.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "쿠폰 json 데이터 전달 성공",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.class))
        )
    })
    @GetMapping("/user")
    public CouponResponse requestCoupons(@CurrentUser UserPrincipal userPrincipal) {

        Member member = memberRepository.findById(userPrincipal.getId()).get();

        List<Coupon> couponList = couponService.getCoupons(member);
        int numOfCoupons = couponList.size();

        return CouponResponse.builder()
                .myCoupons(couponList)
                .numOfCoupons(numOfCoupons)
                .build();
    }

    @Operation(summary = "관리자 쿠폰 페이지 요청", description = "관리자가 쿠폰 페이지를 요청한 경우, 쿠폰을 보유한 모든 유저를 보여줍니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "쿠폰 보유한 유저 json 데이터 전달 성공",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserWithCouponDto.class)))
            )
    })
    @GetMapping("/manager")
    public List<UserWithCouponDto> requestUserWithCoupon() {

        return couponService.getUserWithCoupon();
    }

    @Operation(summary = "관리자 쿠폰 사용 완료 처리 요청", description = "관리자가 특정 쿠폰에 대해 사용 완료 처리 요청을 합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "쿠폰 사용 완료 처리 성공"
            )
    })
    @PatchMapping("/{couponId}")
    public ResponseEntity<Void> requestCompletionOfCouponUse(@PathVariable("couponId") long couponId) {

        couponService.completeUseOfCoupon(couponId);
        return ResponseEntity.noContent().build();
    }
}
