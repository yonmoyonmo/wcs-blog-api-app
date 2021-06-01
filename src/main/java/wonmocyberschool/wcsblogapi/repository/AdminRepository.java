package wonmocyberschool.wcsblogapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wonmocyberschool.wcsblogapi.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long > {
    public Admin findByAdminEmail(String adminEmail);
    public boolean existsByAdminEmail(String adminEmail);
}
