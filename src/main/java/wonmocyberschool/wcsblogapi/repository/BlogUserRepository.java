package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.BlogUser;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> {
    BlogUser findByEmail(String email);
    BlogUser findByNickname(String nickname);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}
