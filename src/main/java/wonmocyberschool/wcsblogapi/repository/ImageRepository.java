package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.Image;
import wonmocyberschool.wcsblogapi.entity.Post;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Image i WHERE i.post = :post")
    void deleteRelatedImages(@Param("post") Post post);
}
