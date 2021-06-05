package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.BlogUser;
import wonmocyberschool.wcsblogapi.entity.Comment;
import wonmocyberschool.wcsblogapi.entity.Post;
import wonmocyberschool.wcsblogapi.repository.BlogUserRepository;
import wonmocyberschool.wcsblogapi.repository.CommentRepository;
import wonmocyberschool.wcsblogapi.repository.PostRepository;

import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentRepository commentRepository;
    private final BlogUserRepository blogUserRepository;
    private final PostRepository postRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository,
                          BlogUserRepository blogUserRepository,
                          PostRepository postRepository){
        this.commentRepository = commentRepository;
        this.blogUserRepository = blogUserRepository;
        this.postRepository = postRepository;
    }

    public boolean createComment(String email, Comment comment){
        BlogUser user = blogUserRepository.findByEmail(email);
        if(user == null){
            logger.error("this email not matched : " + email);
            return false;
        }
        Long postId = comment.getPostId();
        if(postId == null){
            logger.error("post Id == null 왜! 시발!");
            return false;
        }
        Optional<Post> postOptional = postRepository.findById(postId);
        if(!postOptional.isPresent()){
            logger.error("no post with Id : "+postId);
            return false;
        }else{
            comment.setPost(postOptional.get());
            comment.setBlogUser(user);
            comment.setCreatedTime(new Date());
            comment.setUpdatedTime(new Date());
            commentRepository.save(comment);
            return true;
        }
    }
}
