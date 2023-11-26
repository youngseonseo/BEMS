package energypa.bems.chat.repository;

import energypa.bems.chat.entity.ChatRoom;
import energypa.bems.chat.entity.Matching;
import energypa.bems.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    Optional<Matching> findByChatRoomAndMember(ChatRoom chatRoom, Member member);
}