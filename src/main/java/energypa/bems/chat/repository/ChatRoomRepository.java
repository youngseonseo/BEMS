package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select count(cr) from ChatRoom cr")
    int getNumOfChatRoom();

    @Query("select cr from ChatRoom cr")
    ChatRoom findChatRoom();

    @Transactional
    @Modifying
    @Query("update ChatRoom cr set cr.count = cr.count+1 where cr.roomId = :roomId")
    void updateCount(@Param("roomId") String roomId);
}