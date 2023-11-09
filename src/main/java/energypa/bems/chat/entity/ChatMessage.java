package energypa.bems.chat.entity;

import energypa.bems.login.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "채팅 메시지 info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Schema(description = "채팅메시지 고유 번호", example = "1")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @Schema(description = "채팅방 고유 번호", example = "1")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Schema(description = "채팅메시지 타입", example = "TALK")
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Schema(description = "채팅메시지 전송자", example = "1")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member sender;

    @Schema(description = "채팅메시지", example = "안녕하세요~")
    @Column
    private String content;

    @Schema(description = "채팅메시지 전송 시각", example = "")
    @Column(name = "sent_time")
    private String sentTime; // 필드 타입 변경 필요
}