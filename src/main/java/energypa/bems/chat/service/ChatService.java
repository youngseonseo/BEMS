package energypa.bems.chat.service;

import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.entity.Matching;
import energypa.bems.chat.entity.MessageType;
import energypa.bems.chat.repository.ChatMessageRepository;
import energypa.bems.chat.repository.ChatRoomRepository;
import energypa.bems.chat.repository.MatchingRepository;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import energypa.bems.login.domain.Member;
import energypa.bems.login.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final MatchingRepository matchingRepository;
    private final ChatMessageRepository chatMsgRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public List<ChatMessage> enterChatRoom(String roomId, @CurrentUser UserPrincipal userPrincipal) throws IllegalStateException {

        Optional<ChatRoom> chatRoomOp = chatRoomRepository.findChatRoom(roomId);
        if (chatRoomOp.isEmpty()) {
            throw new IllegalStateException("아이디가 " + roomId + "인 채팅방은 존재하지 않습니다!");
        }
        ChatRoom chatRoom = chatRoomOp.get();

        Member member = memberRepository.findById(userPrincipal.getId()).get();

        Optional<Matching> matchingYn = matchingRepository.findByChatRoomAndMember(chatRoom, member);

        if (matchingYn.isEmpty()) { // 유저가 채팅방에 처음 입장한 경우

            Matching matching = Matching.builder()
                    .chatRoom(chatRoom)
                    .member(member)
                    .build();
            matchingRepository.save(matching);

            chatRoomRepository.updateCount(roomId);

            String enterMsg = member.getUsername() + " 님이 방에 입장하였습니다";
            messagingTemplate.convertAndSend("/sub/chatroom/" + chatRoom.getRoomId(), enterMsg); // 해당 채팅방에 존재하는 유저에게 입장메시지 뿌리기

            ChatMessage chatMessage = ChatMessage.builder()
                    .chatRoom(chatRoom)
                    .type(MessageType.ENTER)
                    .sender(null)
                    .content(enterMsg)
                    .sentTime(LocalDateTime.now().toString())
                    .build();
            chatMsgRepository.save(chatMessage);

            return null;

        } else { // 유저가 채팅방에 재입장한 경우
            return chatMsgRepository.getPrevChatMsgs(chatRoom.getRoomId(),
                    member.getUsername() + " 님이 방에 입장하였습니다");
        }
    }
}
