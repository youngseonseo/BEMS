package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository  extends JpaRepository<ChatRoom, String> {

    ChatRoom findByRoomId(String roomId);
    List<ChatRoom> findByRoomName(String roomName);
    ChatRoom save(ChatRoom chatRoom);
    void delete(String roomId);
}
