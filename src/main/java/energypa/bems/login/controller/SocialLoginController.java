package energypa.bems.login.controller;

import energypa.bems.login.domain.Member;
import energypa.bems.login.payload.KakaoDTO;
import energypa.bems.login.service.AuthService;
import energypa.bems.login.service.CustomDefaultOAuth2UserService;
import energypa.bems.login.service.KakaoOauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


/**
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
@Tag(name="social", description = "소셜 로그인 API")
public class SocialLoginController {

    private final KakaoOauthService kakaoOauthService;
    private final CustomDefaultOAuth2UserService oAuth2UserService;


    @Operation(method = "get", summary = "카카오 소셜 로그인 API")
    @PostMapping("/kakao")
    public ResponseEntity<?> kakaoGetToken(@RequestParam Map<String, String> codeMap) {

        String code = codeMap.get("code");

        //Kakao Login 수행 과정
        String accessToken = kakaoOauthService.getKakaoAccessToken(code);
        KakaoDTO kakaoDTO = kakaoOauthService.createKakaoUser(accessToken);

        System.out.println("kakaoDTO.getEmail() = " + kakaoDTO.getEmail());
        //로그인한 카카오 이메일이 로컬 계정으로 등록되어있다면



    }
}
 */
