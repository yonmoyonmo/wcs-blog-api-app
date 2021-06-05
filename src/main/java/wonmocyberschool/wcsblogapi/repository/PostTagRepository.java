package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.Post;
import wonmocyberschool.wcsblogapi.entity.PostTagRelation;
import wonmocyberschool.wcsblogapi.entity.Tag;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTagRelation, Long> {
    List<PostTagRelation> findAllByTag(Tag tag);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostTagRelation p WHERE p.post = :post")
    void deleteRelatedTags(@Param("post") Post post);
}
