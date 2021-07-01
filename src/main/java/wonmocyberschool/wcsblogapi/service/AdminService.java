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
import wonmocyberschool.wcsblogapi.entity.Notification;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.repository.AdminRepository;
import wonmocyberschool.wcsblogapi.repository.CategoryRepository;
import wonmocyberschool.wcsblogapi.repository.NotificationRepository;

import java.util.*;

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

    //----------------wonmo service------------------
    public String addWonmo(Wonmo wonmo){
        Admin newAdmin = new Admin();
        newAdmin.setPassword(passwordEncoder.encode(wonmo.getWonmokey()));
        newAdmin.setAdminEmail(wonmo.getWonmo());
        try{
            adminRepository.save(newAdmin);
        }catch (Exception e){
            logger.info(e.toString());
        }
        return "wonmo added";
    }

    public Admin logginWonmo(Wonmo wonmo){
        Admin admin = adminRepository.findByAdminEmail(wonmo.getWonmo());
        if(admin == null){
            return null;
        }
        if(passwordEncoder.matches(wonmo.getWonmokey(), admin.getPassword())){
            return admin;
        }else{
            return null;
        }
    }

    //----------------category service------------------
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

    public boolean updateCategory(Category category){
        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
        if(categoryOptional.isPresent()){
            categoryOptional.get().setTitle(category.getTitle());
            categoryOptional.get().setDescription(category.getDescription());
            categoryRepository.save(categoryOptional.get());
            return true;
        }else{
            logger.error("not matched category, given category id : "+ category.getId().toString());
            return false;
        }
    }

    public boolean deleteCategory(Category category){
        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
        if(categoryOptional.isPresent()){
            categoryRepository.deleteById(category.getId());
            return true;
        }else{
            logger.error("not matched category, given category id : "+ category.getId().toString());
            return false;
        }
    }

    public List<Category> readCategoryList(){
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryRepository.findAll();
        }catch (Exception e){
            logger.info(e.getMessage()+" : R_Cate "+e);
        }
        return categories;
    }

    //-----------------notification service---------------
    public boolean saveNotification(String email, Notification notification) {
        Admin admin = adminRepository.findByAdminEmail(email);
        notification.setAdmin(admin);
        notification.setCreatedTime(new Date());
        notification.setUpdatedTime(new Date());
        try{
            notificationRepository.save(notification);
            return true;
        }catch (Exception e){
            logger.info(e.getMessage() + " : S_Noti "+e);
            return false;
        }
    }

    public boolean updateNotification(Notification notification) {
        Optional<Notification> notificationOptional = notificationRepository.findById(notification.getId());
        if(notificationOptional.isPresent()){
            notificationOptional.get().setTitle(notification.getTitle());
            notificationOptional.get().setText(notification.getText());
            notificationRepository.save(notificationOptional.get());
            return true;
        }else{
            logger.error("not matched notification, given notification id : "+ notification.getId().toString());
            return false;
        }
    }

    public boolean deleteNotification(Notification notification){
        Optional<Notification> notificationOptional = notificationRepository.findById(notification.getId());
        if(notificationOptional.isPresent()){
            notificationRepository.deleteById(notification.getId());
            return true;
        }else{
            logger.error("not matched notification, given notification id : "+ notification.getId().toString());
            return false;
        }
    }

    public List<Notification> getNotificationList(){
        List<Notification> notifications = new ArrayList<>();
        try{
            notifications = notificationRepository.findAll();
        }catch (Exception e){
            logger.error(e.getMessage() + " : getNotificationList "+e);
        }
        return notifications;
    }
}
