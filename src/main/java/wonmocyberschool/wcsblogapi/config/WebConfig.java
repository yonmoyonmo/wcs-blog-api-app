package wonmocyberschool.wcsblogapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.interceptor.AdminInterceptor;
import wonmocyberschool.wcsblogapi.interceptor.JwtInterceptor;
import wonmocyberschool.wcsblogapi.repository.AdminRepository;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.repository.UserProfileRepository;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private BlogUserRepository blogUserRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:7000","http://localhost:8000","http://localhost:3000",
                "http://wonmocyberschool.com","https://wonmocyberschool.com", "http://localhost:4000")
                .allowedMethods("POST","GET","PUT", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtUtil,blogUserRepository,userProfileRepository))
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/public/**");
        registry.addInterceptor(new AdminInterceptor(jwtUtil, adminRepository))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/wonmo/**", "/admin/test/oauth/**", "/admin/public/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
