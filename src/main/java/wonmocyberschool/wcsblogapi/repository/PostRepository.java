package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.Category;
import wonmocyberschool.wcsblogapi.entity.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByCategory(Category category);
}
