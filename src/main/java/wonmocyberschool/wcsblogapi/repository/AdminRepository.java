package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long > {
    Admin findByAdminEmail(String adminEmail);
    boolean existsByAdminEmail(String adminEmail);
}
