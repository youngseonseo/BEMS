package energypa.bems.login.repository;

import energypa.bems.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    Optional<Member> findByEmail(String email);
    int countByUsername(String username);

    boolean existsByEmail(String email);

}
