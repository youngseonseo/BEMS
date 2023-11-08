package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.entity.ChatRoom;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;

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
            )
    })
    @GetMapping("/chat/enter")
    public List<ChatMessage> enterChatRoom(@CurrentUser UserPrincipal userPrincipal) {

        return chatService.enterChatRoom(userPrincipal);
    }
}