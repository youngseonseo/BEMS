package energypa.bems.manager.repository;

import energypa.bems.manager.domain.Manager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerApplyRepository extends JpaRepository<Manager, Long> {
    @Override
    List<Manager> findAll();

    @Modifying
    @Transactional
    void deleteById(Long id);
}

