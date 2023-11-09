package energypa.bems.chat.repository;

import energypa.bems.chat.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    @Query("select m from Matching m where m.chatRoom.roomId = :roomId and m.member.id = :memberId")
    Optional<Matching> findMatchingStatus(@Param("roomId") String roomId, @Param("memberId") Long memberId);
}