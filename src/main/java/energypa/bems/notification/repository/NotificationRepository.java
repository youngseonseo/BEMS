package energypa.bems.notification.repository;

import energypa.bems.login.domain.Member;
import energypa.bems.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    long countByMemberAndChecked(Member member, boolean checked);

    @Transactional
    List<Notification> findByMemberAndCheckedOrderByCreatedDesc(Member member, boolean b);

    @Transactional
    void deleteByMemberAndChecked(Member member, boolean b);
}