package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.BlogUser;

@Repository
public interface BlogUserRepository extends JpaRepository<BlogUser, Long> {
    BlogUser findByEmail(String email);
    boolean existsByEmail(String email);
}
