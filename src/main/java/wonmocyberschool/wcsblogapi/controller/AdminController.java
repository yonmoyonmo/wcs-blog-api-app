package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //일단 프론트엔드가 없으니 여기서 토큰을 받아 봅니다...
    @GetMapping("/test/oauth")
    public String authTest(HttpServletRequest req, HttpServletResponse res){
        if(req.getParameter("error") != null){
            logger.info("error at /test/oauth");
            logger.info(req.getParameter("error"));
        }else{
            logger.info(req.getParameter("token"));
        }

        return "WCS API server";
    }
}
