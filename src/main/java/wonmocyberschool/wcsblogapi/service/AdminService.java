package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.Admin;
import wonmocyberschool.wcsblogapi.entity.Category;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.repository.AdminRepository;
import wonmocyberschool.wcsblogapi.repository.CategoryRepository;
import wonmocyberschool.wcsblogapi.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private AdminRepository adminRepository;
    private NotificationRepository notificationRepository;
    private CategoryRepository categoryRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            NotificationRepository notificationRepository,
            PasswordEncoder passwordEncoder,
            CategoryRepository categoryRepository
    ){
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder= passwordEncoder;
        this.categoryRepository = categoryRepository;
    }

    //wonmo service
    public String addWonmo(Wonmo wonmo){
        Admin newAdmin = new Admin();
        newAdmin.setPassword(passwordEncoder.encode(wonmo.getWonmokey()));
        newAdmin.setAdminEmail(wonmo.getWonmo());
        try{
            adminRepository.save(newAdmin);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return "wonmo added";
    }

    public Admin logginWonmo(Wonmo wonmo){
        Admin admin = adminRepository.findByAdminEmail(wonmo.getWonmo());
        if(passwordEncoder.matches(wonmo.getWonmokey(), admin.getPassword())){
            return admin;
        }else{
            return null;
        }
    }

    //category service
    public boolean createCategory(String email, Category category){
        Admin admin = adminRepository.findByAdminEmail(email);
        category.setAdmin(admin);
        category.setCreatedTime(new Date());
        try {
            categoryRepository.save(category);
        }catch (Exception e){
            logger.info(e.getMessage() + " : C_Cate");
            return false;
        }
        return true;
    }

    public List<Category> readCategoryList(){
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryRepository.findAll();
        }catch (Exception e){
            logger.info(e.getMessage()+" : R_Cate");
        }
        return categories;
    }



}
