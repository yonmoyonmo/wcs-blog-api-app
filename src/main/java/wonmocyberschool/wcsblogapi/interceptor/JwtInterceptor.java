package wonmocyberschool.wcsblogapi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;
    private final BlogUserRepository blogUserRepository;

    public JwtInterceptor(JwtUtil jwtUtil,BlogUserRepository blogUserRepository){
        this.jwtUtil = jwtUtil;
        this.blogUserRepository = blogUserRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        if(request.getHeader("Authorization") != null) {
            String token = request.getHeader("Authorization");
            logger.info("token : " + token);
            if(!jwtUtil.validateToken(token)){
                logger.info("token invalid");
                return false;
            }else{
                String tokenEmail = jwtUtil.getUserEmailFromToken(token);
                if(blogUserRepository.existsByEmail(tokenEmail)){
                    logger.info("logged in : " + tokenEmail);
                    return true;
                }else{
                    String username = jwtUtil.getUsernameFromToken(token);
                    BlogUser newUser = new BlogUser();
                    newUser.setEmail(tokenEmail);
                    newUser.setUsername(username);
                    newUser.setCreatedTime(new Date());
                    blogUserRepository.save(newUser);
                    logger.info("new user has been registered : " + tokenEmail);
                    return true;
                }
            }
        }else{
            logger.info("no token");
            return false;
        }
    }
}
