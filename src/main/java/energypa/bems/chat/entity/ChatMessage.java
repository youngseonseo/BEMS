package energypa.bems.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long messageId;

    @Column
    private String roomId;

    @Column
    private MessageType type;

    @Column
    private String sender;

    @Column
    private String content;

    @Column
    private LocalDateTime sentTime;
}
