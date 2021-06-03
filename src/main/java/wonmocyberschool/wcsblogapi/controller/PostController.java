package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wonmocyberschool.wcsblogapi.entity.Post;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.PostService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostService postService;
    private final JwtUtil jwtUtil;
    @Autowired
    public PostController(PostService postService,
                          JwtUtil jwtUtil){
        this.postService = postService;
        this.jwtUtil = jwtUtil;
    }
    //post 생성에 필요한 것 : 유저, 제목, 내용텍스트, 이미지, 태그

    @PostMapping("/post")
    public ResponseEntity<Response> createPost(HttpServletRequest request,
            @RequestBody Post post){
        //1. 토큰을 헤더에서 꺼낸 뒤 이메일도 꺼낸 뒤 post 와 함께 서비스로 넘긴다.
        //2. 서비스의 응답대로 처리한다.
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
}
