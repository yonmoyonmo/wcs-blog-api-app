package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.entity.UserProfile;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.BlogUserService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class BlogUserController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BlogUserService blogUserService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BlogUserController(BlogUserService blogUserService,
                              JwtUtil jwtUtil){
        this.blogUserService = blogUserService;
        this.jwtUtil = jwtUtil;
    }

    //로그인 후에 글 쓸라 하는데 닉네임이 없으면 닉네임 만들라고 함.
    //사진 저장할 때 닉네임으로 구별할거임
    //특수문자 안댐(나중에 발리데이션 구현할 때 처리할꼬임)
    @PostMapping("/user/nickname")
    public ResponseEntity<Response> setBlogUserNickname (HttpServletRequest request,
                                                         @RequestBody String nickname){
        Response response = new Response();

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(blogUserService.setBlogUserNickname(email, nickname)){
            response.setSuccess(true);
            response.setMessage("new nickname has been set");
        }else{
            response.setMessage("nickname setting failed");
            response.setSuccess(false);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    //유저 프로파일은 이메일 등록 시 생성되고 그 후로는 업데이트만 가능함.
    @PutMapping("/user/profile")
    public ResponseEntity<Response> updateBlogUserProfile(
            HttpServletRequest request,
            @RequestBody UserProfile userProfile
            ){
        Response response = new Response();

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(blogUserService.updateBlogUserProfile(email, userProfile)){
            response.setSuccess(true);
            response.setMessage("new profile has been set");
        }else{
            response.setSuccess(false);
            response.setMessage("failed");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }


}
