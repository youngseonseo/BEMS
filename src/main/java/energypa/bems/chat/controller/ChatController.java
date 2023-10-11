package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.entity.MessageType;
import energypa.bems.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessageSendingOperations messagingTemplate;

    @PostMapping
    public ChatRoom createRoom(@RequestParam String roomName) {
        return chatService.createRoom(roomName);
    }

    @GetMapping
    public List<ChatRoom> findAllRooms() {
        return chatService.findAllRooms();
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