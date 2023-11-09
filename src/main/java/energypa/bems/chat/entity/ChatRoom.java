package energypa.bems.chat.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "채팅방 info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Schema(description = "채팅방 고유 번호", example = "12345678-1234-5678-1234-567812345678")
    @Id
    @Column(name = "room_id")
    private String roomId;

    @Schema(description = "채팅방 유저 수", example = "4")
    @Column
    private int count;
}