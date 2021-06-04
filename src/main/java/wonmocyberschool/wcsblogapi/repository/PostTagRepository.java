package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wonmocyberschool.wcsblogapi.entity.PostTagRelation;
import wonmocyberschool.wcsblogapi.entity.Tag;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTagRelation, Long> {
    List<PostTagRelation> findAllByTag(Tag tag);
}
