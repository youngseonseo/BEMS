package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.entity.MessageType;
import energypa.bems.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @Operation(summary = "채팅방 생성 요청", description = "유저가 입력한 이름을 가진 채팅방 생성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 생성 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatRoom.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "방 생성 실패",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ChatRoom createRoom(@RequestParam("roomName") String roomName) {
        return chatService.createRoom(roomName);
    }

    @Operation(summary = "전체 채팅방 조회 요청", description = "유저가 포함된 전체 채팅방 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 전체 조회 성공",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ChatRoom.class)))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "방 전체 조회 실패",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping
    public List<ChatRoom> findRoomByRoomName(@RequestParam("roomName") String roomName) {
        return chatService.findAllRooms();
    }

    @Operation(summary = "특정 채팅방 조회 요청", description = "유저가 입력한 이름을 가진 채팅방 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "방 조회 성공",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatRoom.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "방 조회 실패",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ChatRoom createChatRoom(@RequestParam("roomName") String roomName) {
        return null;
    }

    // ws://localhost:8080/ws-stomp/pub/chat/message라는 발행 요청 + 데이터
    // message의 구성: roomId, sender, sentTime, content, type
    @MessageMapping("/message")
    public void message(ChatMessage chatMessage) {
        if (chatMessage.getType().equals(MessageType.ENTER)) {
            chatMessage.setContent(chatMessage.getSender() + "님이 입장하셨습니다.");
        } else if (chatMessage.getType().equals(MessageType.QUIT)) {
            chatMessage.setContent(chatMessage.getSender() + "님이 퇴장하셨습니다.");
        }
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
    }
}