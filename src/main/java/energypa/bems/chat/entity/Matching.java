package energypa.bems.chat.entity;

import energypa.bems.login.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "채팅방-유저 간 매칭 info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Matching {

    @Schema(description = "매칭 고유 번호", example = "1")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "matching_id")
    private Long matchingId;

    @Schema(description = "채팅방 고유 번호", example = "12345678-1234-5678-1234-567812345678")
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Schema(description = "유저 고유 번호", example = "1")
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
