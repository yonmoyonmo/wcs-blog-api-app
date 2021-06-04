package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.entity.UserProfile;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.repository.UserProfileRepository;

@Service
public class BlogUserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BlogUserRepository blogUserRepository;
    private final UserProfileRepository userProfileRepository;
    @Autowired
    public BlogUserService(BlogUserRepository blogUserRepository,
                           UserProfileRepository userProfileRepository){
        this.blogUserRepository = blogUserRepository;
        this.userProfileRepository = userProfileRepository;
    }


    public boolean setBlogUserNickname(String email, String nickname) {
        BlogUser user = blogUserRepository.findByEmail(email);
        if(user == null){
            logger.error("시발 왜 유저가 없지? : "+ email);
            return false;
        }
        user.setNickname(nickname);
        try {
            blogUserRepository.save(user);
        }catch (Exception e){
            logger.error(e.getMessage()+" : at saveBlogUserNickname");
            return false;
        }
        return true;
    }

    public boolean updateBlogUserProfile(String email, UserProfile userProfile) {
        BlogUser user = blogUserRepository.findByEmail(email);
        if(user == null){
            logger.error("시발 왜 유저가 없지? : "+ email);
            return false;
        }
        userProfile.setOwner(user);
        try{
            userProfileRepository.save(userProfile);
            blogUserRepository.save(user);
        }catch (Exception e){
            logger.error(e.getMessage()+" : profile update");
            return false;
        }
        return true;
    }
}