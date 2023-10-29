package energypa.bems.login.payload;

import energypa.bems.login.domain.Authority;
import energypa.bems.login.domain.Member;
import energypa.bems.login.domain.Provider;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class KakaoDTO {

    private String email;
    private String nickName;
    private String imageUrl;

    public Member toEntity(){
        return Member.builder()
                .password(getRamdomPassword(15))    // 초기 비밀번호 랜덤 15자리수 로 세팅
                .email(email)
                .username(nickName)
                .provider(Provider.kakao)
                .authority(Authority.USER)
                .imageUrl(imageUrl)
                .build();
    }

    public String getRamdomPassword(int size) {         // 랜덤으로 생성한 소셜 로그인 비밀번호에 BCrypt로 암호화
        String pw = RandomStringUtils.randomAlphanumeric(size);
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(pw, salt);
    }
}
