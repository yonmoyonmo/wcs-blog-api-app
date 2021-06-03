package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.*;
import wonmocyberschool.wcsblogapi.repository.*;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostRepository postRepository;
    private final BlogUserRepository blogUserRepository;
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public PostService(PostRepository postRepository,
                       BlogUserRepository blogUserRepository,
                       ImageRepository imageRepository,
                       TagRepository tagRepository,
                       CategoryRepository categoryRepository){
        this.postRepository = postRepository;
        this.blogUserRepository = blogUserRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    public boolean savePost(Post post, String email){
        //인터셉터에서 검증한 토큰이므로 이메일로 찾으면 백퍼 있음
        BlogUser blogUser = blogUserRepository.findByEmail(email);
        if(blogUser == null){
            logger.error("오우시발");
            return false;
        }
        Optional<Category> categoryOptional = categoryRepository.findById(post.getCategoryId());
        Category category = categoryOptional.orElse(null);
        post.setBlogUser(blogUser);
        post.setCategory(category);

        //이미지와 태그를 임시 필드에서 빼가지고 영속화해야함...
        List<String> tagList = post.getTagList();
        List<String> imageUrlList = post.getImageUrlList();
        if(saveTags(tagList, post) && saveImages(imageUrlList, post)){
            System.out.println("let's see the post");
            System.out.println(post.getTags());
            System.out.println(post.getImages());
            System.out.println(post.getCategory().getTitle());
            System.out.println(post.getBlogUser().getEmail());

            post.setCreatedTime(new Date());
            postRepository.save(post);
            return true;
        }else{
            logger.error("at savePost");
            return false;
        }
    }
    private boolean saveTags(List<String> tagList, Post post){
        try{
            for(String tag : tagList){
                Tag newTag = new Tag();
                newTag.setTagName(tag);
                newTag.setPost(post);
                newTag.setCreatedTime(new Date());
                tagRepository.save(newTag);
            }
        }catch (Exception e){
            logger.error(e.getMessage() + " : while saving tags at savePost");
            return false;
        }
        logger.info("tags are saved");
        return true;
    }
    private boolean saveImages(List<String> imageUrlList, Post post){
        try{
            for(String imageUrl : imageUrlList){
                Image newImage = new Image();
                newImage.setImageURI(imageUrl);
                newImage.setPost(post);
                newImage.setCreatedTime(new Date());
                imageRepository.save(newImage);
            }
        }catch (Exception e){
            logger.error(e.getMessage() + " : while saving images at savePost");
            return false;
        }
        return true;
    }
}
