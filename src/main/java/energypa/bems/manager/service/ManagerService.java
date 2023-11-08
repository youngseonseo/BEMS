package energypa.bems.manager.service;

import energypa.bems.login.advice.assertThat.DefaultAssert;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Authority;
import energypa.bems.login.domain.Member;
import energypa.bems.login.domain.Token;
import energypa.bems.login.payload.response.ApiResponse;
import energypa.bems.login.payload.response.Message;
import energypa.bems.login.repository.MemberRepository;
import energypa.bems.manager.domain.Manager;
import energypa.bems.manager.dto.ManagerApplyListDto;
import energypa.bems.manager.repository.ManagerApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final MemberRepository memberRepository;
    private final ManagerApplyRepository managerApplyRepository;

    public ResponseEntity<?> apply(UserPrincipal userPrincipal){
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(member.isPresent(), "유저가 올바르지 않습니다.");
        Member user = member.get();
        if(user.getAuthority()!= Authority.USER){
            throw new IllegalArgumentException("유저는 이미 건물 관리자입니다.");
        }

        managerApplyRepository.save(new Manager(user));
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("건물 관리자 신청이 완료되었습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> enroll(UserPrincipal userPrincipal, Long managerId){
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(member.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Manager> applyManager = managerApplyRepository.findById(managerId);
        DefaultAssert.isTrue(applyManager.isPresent(), "관리자를 신청 id가 올바르지 않습니다.");

        memberRepository.updateAuthority(applyManager.get().getMember().getId(), Authority.MANAGER);   // 해당 유저 매니저로 등록
        managerApplyRepository.deleteById(managerId);       // 신청 목록에서 해당 유저 삭제
        ApiResponse apiResponse = ApiResponse.builder().check(true).information(Message.builder().message("건물 관리자 등록이 완료되었습니다.").build()).build();

        return ResponseEntity.ok(apiResponse);
    }


    public ResponseEntity<?> applyList(UserPrincipal userPrincipal){
        Optional<Member> member = memberRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(member.isPresent(), "유저가 올바르지 않습니다.");
        /**
        if(member.get().getAuthority()==Authority.USER){
            throw new IllegalArgumentException("건물 관리자만 리스트를 볼 수 있습니다.");
        }
         */

        List<ManagerApplyListDto> list = new ArrayList<>();
        List<Manager> memberList = managerApplyRepository.findAll();
        for (Manager manager : memberList) {
            Long id = manager.getId();
            Member member1 = manager.getMember();
            ManagerApplyListDto managerDto = new ManagerApplyListDto(id, member1.getId(), member1.getUsername(), member1.getEmail());
            list.add(managerDto);
        }

        return ResponseEntity.ok(list);
    }
}
