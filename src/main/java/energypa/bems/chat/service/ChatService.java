package energypa.bems.chat.service;

import energypa.bems.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private Map<String, ChatRoom> rooms = new LinkedHashMap<>();

    public ChatRoom createRoom(String roomName) {
        String roomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = new ChatRoom(roomId, roomName);
        rooms.put(roomId, chatRoom);
        return chatRoom;
    }

    public List<ChatRoom> findAllRooms() {
        List<ChatRoom> chatRooms = new ArrayList<>(rooms.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    public ChatRoom findRoom(String roomId) {
        return rooms.get(roomId);
    }
}
