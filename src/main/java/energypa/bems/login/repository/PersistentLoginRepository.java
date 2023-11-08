package energypa.bems.login.repository;

import energypa.bems.login.domain.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, Long> {

    PersistentLogin findBySeries(String series);
    List<PersistentLogin> findByUsername(String username);
    Iterable<PersistentLogin> findByLastUsedAfter(Date expiration);
}
