package wonmocyberschool.wcsblogapi.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.entity.UserProfile;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.repository.UserProfileRepository;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class JwtInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtUtil jwtUtil;
    private final BlogUserRepository blogUserRepository;
    private final UserProfileRepository userProfileRepository;

    public JwtInterceptor(JwtUtil jwtUtil,BlogUserRepository blogUserRepository
            ,UserProfileRepository userProfileRepository){
        this.jwtUtil = jwtUtil;
        this.blogUserRepository = blogUserRepository;
        this.userProfileRepository = userProfileRepository;
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

                    UserProfile userProfile = new UserProfile();
                    userProfile.setCreatedTime(new Date());
                    userProfile.setOwner(newUser);

                    blogUserRepository.save(newUser);
                    userProfileRepository.save(userProfile);


                    logger.info("new user has been registered : " + tokenEmail);
                    return true;
                }
            }
        }else{
            logger.info("no token, 인터셉터가 처리했으니 안심하라구!");
            return false;
        }
    }
}
