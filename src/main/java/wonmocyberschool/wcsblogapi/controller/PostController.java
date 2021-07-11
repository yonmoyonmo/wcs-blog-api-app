package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

        logger.info("at : /post : POST");

        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(post.getCategoryId() == null){
            logger.info("category id 없음");
            response.setMessage("category id 없음");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
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

    @PutMapping("/post")
    public ResponseEntity<Response> updatePost(HttpServletRequest request,
                                               @RequestBody Post post){
        logger.info("at : /post : PUT");

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);

        Response response = new Response();

        if(post.getId() == null){
            response.setMessage("no post id");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        if(!postService.updatePost(email, post)){
            response.setMessage("something went wrong");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("post updated");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }

    }

    @DeleteMapping("/post")
    public ResponseEntity<Response> deletePost(HttpServletRequest request,
                                               @RequestBody Post post){
        logger.info("at : /post : DELETE");

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);

        Response response = new Response();
        Long postId = post.getId();
        if(postId == null){
            response.setMessage("there's no post id");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        if(!postService.deleteByPostId(email, postId)){
            response.setMessage("delete failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("delete success");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/public/post/category/{categoryId}")
    public ResponseEntity<Response> getPostsByCategory(@PathVariable("categoryId") Long categoryId){
        logger.info("at : /public/post/category/{categoryId} : GET");

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

    @GetMapping("/public/post/{id}")
    public ResponseEntity<Response> getOnePost(@PathVariable("id") Long postId){
        logger.info("at : /public/post/{id} : GET");

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

    @GetMapping("/public/post/tag/{tagId}")
    public ResponseEntity<Response> getPostsByTagId(
            @PathVariable("tagId") Long tagId){

        logger.info("at : /public/post/tag/{tagId} : GET");

        Response response = new Response();
        List<Post> posts = postService.getPostsByTagId(tagId);
        if(posts.isEmpty()){
            response.setMessage("no posts");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else if(posts == null){
            response.setMessage("something went wrong");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(posts);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

}

















