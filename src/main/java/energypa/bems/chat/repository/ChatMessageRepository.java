package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("select cm from ChatMessage cm where cm.chatRoom.roomId = :roomId " +
            "and cm.messageId > (select cm.messageId from ChatMessage cm where cm.chatRoom.roomId = :roomId and cm.sender.id is null and cm.content = :content)")
    List<ChatMessage> getPrevChatMsgs(@Param("roomId") String roomId, @Param("content") String content);
}
