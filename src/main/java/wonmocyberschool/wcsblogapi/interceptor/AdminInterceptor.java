package wonmocyberschool.wcsblogapi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import wonmocyberschool.wcsblogapi.repository.AdminRepository;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class AdminInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JwtUtil jwtUtil;
    private AdminRepository adminRepository;

    @Autowired
    public AdminInterceptor(JwtUtil jwtUtil, AdminRepository adminRepository) {
        this.jwtUtil = jwtUtil;
        this.adminRepository = adminRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization");
            logger.info("token : " + token);
            if(!jwtUtil.validateToken(token)){
                logger.info("token invalid");
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"token invalid\"}");
                return false;
            }else{
                String tokenEmail = jwtUtil.getUserEmailFromToken(token);
                return adminRepository.existsByAdminEmail(tokenEmail);
            }
        }else{
            logger.info("no token, 인터셉터가 처리했으니 안심하라구!");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"token invalid\"}");
            return false;
        }
    }
}
