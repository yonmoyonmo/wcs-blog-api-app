package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wonmocyberschool.wcsblogapi.entity.Admin;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.repository.AdminRepository;
import wonmocyberschool.wcsblogapi.repository.NotificationRepository;

@Service
public class AdminService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private  AdminRepository adminRepository;
    private  NotificationRepository notificationRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(
            AdminRepository adminRepository,
            NotificationRepository notificationRepository,
            PasswordEncoder passwordEncoder
    ){
        this.adminRepository = adminRepository;
        this.notificationRepository = notificationRepository;
        this.passwordEncoder= passwordEncoder;
    }



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

}
