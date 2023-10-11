package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository  extends JpaRepository<ChatMessage, Long> {

    ChatMessage findByMessageId(Long messageId);
    ChatMessage save(ChatMessage chatMessage);
}
