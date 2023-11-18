package energypa.bems.coupon.controller;

import energypa.bems.coupon.dto.CouponResponse;
import energypa.bems.coupon.entity.Coupon;
import energypa.bems.coupon.service.CouponService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "coupon", description = "쿠폰 API")
@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final MemberRepository memberRepository;

    @Operation(summary = "쿠폰함 페이지 요청", description = "유저가 '나의 쿠폰함 페이지'를 요청합니다.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "쿠폰 json 데이터 전달 성공",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = CouponResponse.class))
        )
    })
    @GetMapping
    public CouponResponse requestCoupons(@CurrentUser UserPrincipal userPrincipal) {

        Member member = memberRepository.findById(userPrincipal.getId()).get();

        List<Coupon> couponList = couponService.getCoupons(member);
        int numOfCoupons = couponService.getNumOfCoupons(member);

        return CouponResponse.builder()
                .myCoupons(couponList)
                .numOfCoupons(numOfCoupons)
                .build();
    }
}
