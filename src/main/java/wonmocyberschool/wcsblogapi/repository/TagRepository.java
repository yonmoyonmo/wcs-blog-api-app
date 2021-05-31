package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
