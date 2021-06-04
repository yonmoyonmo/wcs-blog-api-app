package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wonmocyberschool.wcsblogapi.entity.PostTagRelation;

public interface PostTagRepository extends JpaRepository<PostTagRelation, Long> {
}
