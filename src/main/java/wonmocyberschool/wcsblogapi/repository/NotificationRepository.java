package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
