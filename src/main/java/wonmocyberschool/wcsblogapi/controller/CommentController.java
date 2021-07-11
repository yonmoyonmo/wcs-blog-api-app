package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.entity.Comment;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.CommentService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentService commentService;
    private final JwtUtil jwtUtil;
    @Autowired
    public CommentController(CommentService commentService,
                             JwtUtil jwtUtil){
        this.commentService = commentService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/comment")
    public ResponseEntity<Response> addCommentToPost(HttpServletRequest request,
            @RequestBody Comment comment){

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }

        logger.info("creating comment... "+ clientIp);

        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);

        Response response = new Response();

        if(!commentService.createComment(email, comment)){
            response.setSuccess(false);
            response.setMessage("댓글 달기 실패!");
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            logger.info("creating comment success : "+ clientIp);
            response.setSuccess(true);
            response.setMessage("댓글 달기 성공!");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
    @PutMapping("/comment")
    public ResponseEntity<Response> updateComment(HttpServletRequest request,
            @RequestBody Comment comment){

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        logger.info("updating comment... "+ clientIp);

        Response response = new Response();
        if(comment.getId()==null){
            response.setMessage("request has no comment id");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(!commentService.updateComment(email, comment)){
            response.setSuccess(false);
            response.setMessage("comment update failed");
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            logger.info("updating comment success : "+ clientIp);
            response.setSuccess(true);
            response.setMessage("comment update success");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping("/comment")
    public ResponseEntity<Response> deleteComment(HttpServletRequest request,
                                                  @RequestBody Comment comment){

        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }

        logger.info("deleting comment... "+ clientIp);

        Response response = new Response();
        if(comment.getId()==null){
            response.setMessage("request has no comment id");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.BAD_REQUEST);
        }
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(!commentService.deleteComment(email, comment)){
            response.setMessage("comment delete failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            logger.info("deleting comment success "+ clientIp);
            response.setMessage("comment delete success");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

}
