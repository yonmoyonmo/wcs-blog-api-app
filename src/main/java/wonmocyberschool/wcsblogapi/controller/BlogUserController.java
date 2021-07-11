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

    @PostMapping("/user/nickname")
    public ResponseEntity<Response> setBlogUserNickname (HttpServletRequest request,
                                                         @RequestBody String nickname){
        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(blogUserService.setBlogUserNickname(email, nickname)){
            logger.info(nickname + " new nickname");
            response.setSuccess(true);
            response.setMessage("new nickname has been set");
        }else{
            response.setMessage("nickname setting failed");
            response.setSuccess(false);
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    ///user/nickname/check?nickname=abc
    @GetMapping("/user/nickname/check")
    public ResponseEntity<Response> nicknameDubCheck(@RequestParam("nickname") String nickname, HttpServletRequest request){
        Response response = new Response();
        logger.info("checking nickname");
        if(blogUserService.checkNickname(nickname)){
            response.setSuccess(false);
            response.setMessage("이미 존재하는 닉네임");
        }else{
            response.setSuccess(true);
            response.setMessage("사용할 수 있는 닉네임");
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
            logger.info("user profile set : " + userProfile.getOwner().getEmail());
            response.setSuccess(true);
            response.setMessage("new profile has been set");
        }else{
            response.setSuccess(false);
            response.setMessage("failed");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }
    @GetMapping("/user/profile")
    public ResponseEntity<Response> getUserProfile(HttpServletRequest request){
        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        UserProfile userProfile = blogUserService.getUserProfile(email);
        if(userProfile == null){
            response.setSuccess(false);
            response.setMessage("없는데여?");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            logger.info("/user/profile : "+ email);
            response.setMessage("ok");
            response.setSuccess(true);
            response.setData(userProfile);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }


    @GetMapping("/user/profile/{id}")
    public ResponseEntity<Response> getUserProfileById(@PathVariable("id") Long profileId,
                                                       HttpServletRequest request){
        Response response = new Response();
        UserProfile userProfile = blogUserService.getUserProfileById(profileId);
        if(userProfile == null){
            response.setSuccess(false);
            response.setMessage("없는데여?");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            logger.info("/user/profile/{id} : " + profileId);
            response.setMessage("ok");
            response.setSuccess(true);
            response.setData(userProfile);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
}
