package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.*;
import wonmocyberschool.wcsblogapi.repository.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostRepository postRepository;
    private final BlogUserRepository blogUserRepository;
    private final ImageRepository imageRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;
    private final PostTagRepository postTagRepository;
    @Autowired
    public PostService(PostRepository postRepository,
                       BlogUserRepository blogUserRepository,
                       ImageRepository imageRepository,
                       TagRepository tagRepository,
                       CategoryRepository categoryRepository,
                       PostTagRepository postTagRepository){
        this.postRepository = postRepository;
        this.blogUserRepository = blogUserRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.postTagRepository = postTagRepository;
    }

    public Page<Post> getPostsByCategory(Long categoryId, int page){
        Optional<Category> category = categoryRepository.findById(categoryId);
        PageRequest pageRequest = PageRequest.of(page, 12, Sort.by("id").descending());
        if(category.isPresent()) {
            try {
                return postRepository.findAllByCategory(category.get(), pageRequest);
            } catch (Exception e) {
                logger.error(e.getMessage() + " : getPostsByCate : "+e);
                return null;
            }
        }else return null;
    }

    public Optional<Post> getPostById(Long postId){
        try{
            return postRepository.findById(postId);
        }catch (Exception e){
            logger.error(e.getMessage() + " : getPostById : "+e);
            return null;
        }
    }

    public List<Post> getPostsByTagId(Long tagId){
        Optional<Tag> tagOptional = tagRepository.findById(tagId);
        List<Post> posts = new ArrayList<>();
        if(tagOptional.isPresent()){
            try {
                Tag tag = tagOptional.get();
                List<PostTagRelation> postTagRelations = postTagRepository.findAllByTag(tag);
                for (PostTagRelation postTagRelation : postTagRelations) {
                    if(!posts.contains(postTagRelation.getPost())){
                        posts.add(postTagRelation.getPost());
                    }
                }
                return posts;
            }catch (Exception e){
                logger.error(e +" : getPostsByTagId");
                return null;
            }
        }else{
            logger.info("there's no tag with id : " + tagId);
            return posts;
        }
    }

    public boolean deleteByPostId(String email, Long postId){
        Optional<Post> targetPostOptional = postRepository.findById(postId);
        if(!targetPostOptional.isPresent()){
            logger.error("there's no post with id : "+ postId);
            return false;
        }else{

            Post targetPost = targetPostOptional.get();
            //BlogUser user = blogUserRepository.findByEmail(email);

            //요청하신 분과 포스팅 주인님이 같은지 확인
            if(!targetPost.getBlogUser().getEmail().equals(email)){
                logger.error("email : "+email+
                        "\nthis post's user email : "
                        +targetPost.getBlogUser().getEmail());
                return false;
            }
            postRepository.delete(targetPost);
            return true;
        }
    }

    public boolean updatePost(String email, Post post){
        Optional<Post> existingPostOptional = postRepository.findById(post.getId());

        //이럴 필요 없을듯
        //BlogUser user = blogUserRepository.findByEmail(email);

        if(!existingPostOptional.isPresent()){
            logger.error("no post with id : "+post.getId());
            return false;
        }else{
            //제목, 글 내용, 이미지, 태그
            Post existingPost = existingPostOptional.get();

            //요청하신 분과 포스팅 주인님이 같은지 확인
            if(!existingPost.getBlogUser().getEmail().equals(email)){
                logger.error("email : "+email+
                        "\nthis post's user email : "
                        +existingPost.getBlogUser().getEmail());
                return false;
            }

            existingPost.setTitle(post.getTitle());
            existingPost.setText(post.getText());
            postRepository.save(existingPost);

            try {
                postTagRepository.deleteRelatedTags(existingPost);
                imageRepository.deleteRelatedImages(existingPost);
            }catch (Exception e){
                logger.error(e.toString());
            }

            List<String> tagList = post.getTagList();
            List<String> imageUrlList = post.getImageUrlList();

            if(saveTags(tagList, existingPost) && saveImages(imageUrlList, existingPost)){
                postRepository.save(existingPost);
                return true;
            }else{
                logger.error("at update post");
                return false;
            }

        }

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
        if(category == null){
            logger.error("no category");
            return false;
        }
        post.setCategory(category);
        post.setCreatedTime(new Date());
        postRepository.save(post);

        //이미지와 태그를 임시 필드에서 빼가지고 영속화해야함...
        List<String> tagList = post.getTagList();
        List<String> imageUrlList = post.getImageUrlList();

        if(saveTags(tagList, post) && saveImages(imageUrlList, post)){
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
                if(tagRepository.existsByTagName(tag)){
                    Tag existingTag = tagRepository.findByTagName(tag);
                    PostTagRelation postTagRelation = new PostTagRelation();
                    postTagRelation.setPost(post);
                    postTagRelation.setTag(existingTag);
                    postTagRepository.save(postTagRelation);
                }else{
                    Tag newTag = new Tag();
                    newTag.setTagName(tag);
                    newTag.setCreatedTime(new Date());
                    tagRepository.save(newTag);

                    PostTagRelation postTagRelation = new PostTagRelation();
                    postTagRelation.setPost(post);
                    postTagRelation.setTag(newTag);
                    postTagRepository.save(postTagRelation);
                }
            }
        }catch (Exception e){
            logger.error(e.getMessage() + " : while saving tags at savePost : " + e);
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
            logger.error(e.getMessage() + " : while saving images at savePost : "+e);
            return false;
        }
        return true;
    }
}
