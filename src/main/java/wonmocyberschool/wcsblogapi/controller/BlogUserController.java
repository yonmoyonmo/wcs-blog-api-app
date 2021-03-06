package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.entity.UserProfile;
import wonmocyberschool.wcsblogapi.payload.ProfileView;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.repository.UserProfileRepository;
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
            response.setMessage("?????? ???????????? ?????????");
        }else{
            response.setSuccess(true);
            response.setMessage("????????? ??? ?????? ?????????");
        }
        return new ResponseEntity<Response>(response, HttpStatus.OK);
    }

    //?????? ??????????????? ????????? ?????? ??? ???????????? ??? ????????? ??????????????? ?????????.
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
    @GetMapping("/user/profile")
    public ResponseEntity<Response> getUserProfile(HttpServletRequest request){
        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        UserProfile userProfile = blogUserService.getUserProfile(email);
        if(userProfile == null){
            response.setSuccess(false);
            response.setMessage("?????????????");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            logger.info("/user/profile : "+ email);
            response.setMessage("ok");
            response.setSuccess(true);
            response.setData(userProfile);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    //??????????????? ????????? ???????????? ????????? ??????
    @GetMapping("/public/user/profile")
    public ResponseEntity<Response> getUserProfileById(@RequestParam("nickname") String nickname,
                                                       HttpServletRequest request){
        Response response = new Response();

        ProfileView userProfile = new ProfileView();

        userProfile = blogUserService.getUserProfileByNickName(nickname);


        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        logger.info(clientIp+" someone read this profile : "+ nickname);

        if(userProfile == null){
            response.setSuccess(false);
            response.setMessage("?????????????");
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            logger.info("/public/user/profile : " + nickname);
            response.setMessage("ok");
            response.setSuccess(true);
            response.setData(userProfile);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
}
