package wonmocyberschool.wcsblogapi.service;

import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.entity.UserProfile;
import wonmocyberschool.wcsblogapi.payload.ProfileView;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.repository.UserProfileRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
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
            logger.error("왜 유저가 없지? : "+ email);
            return false;
        }
        if(!blogUserRepository.existsByNickname(nickname)) {
            user.setNickname(nickname);
            user.setUpdatedTime(new Date());
        }else{
            return false;
        }
        try {
            blogUserRepository.save(user);
        }catch (Exception e){
            logger.error(e.getMessage()+" : at saveBlogUserNickname : "+e);
            return false;
        }
        return true;
    }

    public boolean updateBlogUserProfile(String email, UserProfile userProfile) {
        BlogUser user = blogUserRepository.findByEmail(email);
        if(user == null){
            logger.error("왜 유저가 없지? : "+ email);
            return false;
        }
        UserProfile profile = userProfileRepository.findByOwner(user);
        profile.setProfileImageURL(userProfile.getProfileImageURL());
        profile.setDescription(userProfile.getDescription());
        try{
            userProfileRepository.save(profile);
        }catch (Exception e){
            logger.error(e.getMessage()+" : profile update : "+e);
            return false;
        }
        return true;
    }

    public UserProfile getUserProfile(String email){
        BlogUser user = blogUserRepository.findByEmail(email);
        UserProfile targetProfile = user.getProfiles().get(0);
        if(user == null || targetProfile == null){
            logger.error("왜 없지? : "+ email);
            return null;
        }
        return targetProfile;
    }

    public boolean checkNickname(String nickname) {
        return blogUserRepository.existsByNickname(nickname);
    }

    public ProfileView getUserProfileByNickName(String nickname) {
        ProfileView profileView = new ProfileView();

        BlogUser blogUser = blogUserRepository.findByNickname(nickname);
        if(blogUser == null){
            logger.error("no user with "+ nickname);
            return null;
        }
        UserProfile userProfile = userProfileRepository.findByOwner(blogUser);
        if(userProfile == null){
            logger.error("no profile with "+nickname);
            return null;
        }
        profileView.setBlogUser(blogUser);
        profileView.setUserProfile(userProfile);
        return profileView;
    }
}
