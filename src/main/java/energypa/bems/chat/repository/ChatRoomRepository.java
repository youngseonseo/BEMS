package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select count(cr) from ChatRoom cr")
    int getNumOfChatRoom();

    @Query("select cr from ChatRoom cr")
    ChatRoom findChatRoom();
}