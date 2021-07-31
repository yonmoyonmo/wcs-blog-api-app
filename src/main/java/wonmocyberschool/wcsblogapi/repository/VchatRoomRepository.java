package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wonmocyberschool.wcsblogapi.entity.VchatRoom;

public interface VchatRoomRepository extends JpaRepository<VchatRoom, Long> {
}
