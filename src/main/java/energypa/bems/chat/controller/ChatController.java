package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.repository.ChatMessageRepository;
import energypa.bems.chat.repository.ChatRoomRepository;
import energypa.bems.chat.service.ChatService;
import energypa.bems.login.config.security.token.CurrentUser;
import energypa.bems.login.config.security.token.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@Tag(name = "chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate;

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
                    description = "채팅방 입장 성공",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChatMessage.class)))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "존재하지 않는 채팅방 입장 요청"
            )
    })
    @GetMapping("/api/chatrooms/{roomId}")
    public ResponseEntity<List<ChatMessage>> enterChatRoom(@PathVariable("roomId") String roomId, @CurrentUser UserPrincipal userPrincipal) {

        try {
            List<ChatMessage> prevChatMsgs = chatService.enterChatRoom(roomId, userPrincipal);
            return ResponseEntity.ok(prevChatMsgs);
        }
        catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @MessageMapping("/chatroom/{roomId}")
    public void sendChatMsgToSubscriber(@DestinationVariable("roomId") String roomId, ChatMessage chatMessage) {

        log.info("chat '{}' send by '{}'", chatMessage.getContent(), chatMessage.getSender());

        chatMessageRepository.save(chatMessage);

        messagingTemplate.convertAndSend("/sub/chatroom/" + roomId, chatMessage);
    }
}