package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wonmocyberschool.wcsblogapi.entity.Comment;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.CommentService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
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

    //테스트 안해봄
    @PostMapping("/comment")
    public ResponseEntity<Response> addCommentToPost(
            HttpServletRequest request,
            @RequestBody Comment comment
            ){
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);

        Response response = new Response();

        if(!commentService.createComment(email, comment)){
            response.setSuccess(false);
            response.setMessage("댓글 달기 실패!");
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setSuccess(true);
            response.setMessage("댓글 달기 성공!");
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
//    @PutMapping("/comment")
//    public ResponseEntity<Response> updateComment(){
//        //아맞다 포스트 수정, 삭제도 유저 확인 해야하는데?;;
//    }
}
