package energypa.bems.chat.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom {

    @Id
    private String roomId;

    @Column
    private String roomName;
}
