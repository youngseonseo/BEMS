package energypa.bems.chat.controller;

import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.repository.ChatRoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomRepository chatRoomRepository;

    @PostConstruct
    public void init() {
        int roomNum = chatRoomRepository.getNumOfChatRoom();
        if (roomNum == 0) {
            String roomId = UUID.randomUUID().toString();
            ChatRoom chatRoom = new ChatRoom(roomId, 0);
            chatRoomRepository.save(chatRoom);
        }
    }
}