package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.entity.Category;
import wonmocyberschool.wcsblogapi.entity.Post;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.PostService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostService postService;
    private final JwtUtil jwtUtil;
    @Autowired
    public PostController(PostService postService,
                          JwtUtil jwtUtil){
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/post")
    public ResponseEntity<Response> createPost(HttpServletRequest request,
            @RequestBody Post post){
        Response response = new Response();

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        logger.info(email + " : at createPost");
        if(postService.savePost(post, email)){
            response.setMessage("post added");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/post/category/{categoryId}")
    public ResponseEntity<Response> getPostsByCategory(@PathVariable("categoryId") Long categoryId){
        Response response = new Response();
        List<Post> posts = postService.getPostsByCategory(categoryId);
        if(posts.isEmpty()){
            response.setMessage("this category has no post");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else if(posts == null){
            response.setMessage("service went wrong");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } else{
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(posts);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Response> getOnePost(@PathVariable("id") Long postId){
        Response response = new Response();
        Optional<Post> postOptional = postService.getPostById(postId);
        if(postOptional.isPresent()){
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(postOptional.get());
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else if(postOptional == null){
            response.setMessage("service went wrong");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("there's no post with id : " + postId);
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }
    }


}

















