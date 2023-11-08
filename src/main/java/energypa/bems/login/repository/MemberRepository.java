package energypa.bems.login.repository;

import energypa.bems.login.domain.Authority;
import energypa.bems.login.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    int countByUsername(String username);

    List<Member> findAllByAuthority(Authority authority);
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("update Member p set p.authority = :authority where p.id = :id")
    int updateAuthority(@Param("id") Long id, @Param("authority") Authority authority);


}
