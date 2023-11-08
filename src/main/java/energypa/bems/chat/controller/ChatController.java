package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.entity.Matching;
import energypa.bems.chat.repository.ChatRoomRepository;
import energypa.bems.chat.repository.MatchingRepository;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MatchingRepository matchingRepository;

    @PostConstruct
    public void init() {
        int roomNum = chatRoomRepository.getNumOfChatRoom();
        if (roomNum == 0) {
            String roomId = UUID.randomUUID().toString();
            ChatRoom chatRoom = new ChatRoom(roomId, 0);
            chatRoomRepository.save(chatRoom);
        }
    }

    @Operation(summary = "채팅방 입장 요청", description = "유저가 채팅방 처음 입장 또는 재입장을 요청합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "채팅방 입장 성공" //,
//                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatRoom.class))
            )
    })
    @GetMapping("/chat/enter")
    public void enterChatRoom(@CurrentUser UserPrincipal userPrincipal) {

        ChatRoom chatRoom = chatRoomRepository.findChatRoom();
        Member member = memberRepository.findById(userPrincipal.getId()).get();

        Optional<Matching> matchingYn = matchingRepository.findMatchingStatus(chatRoom.getRoomId(), member.getId());

        if (matchingYn.isEmpty()) { // 유저가 채팅방에 처음 입장한 경우
            System.out.println("채팅방에 처음 입장하였습니다!"); //


        } else { // 유저가 채팅방에 재입장한 경우
            System.out.println("채팅방에 재입장하였습니다!"); //

        }
    }
}