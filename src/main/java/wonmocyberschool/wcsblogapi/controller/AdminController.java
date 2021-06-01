package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.config.JwtProperties;
import wonmocyberschool.wcsblogapi.entity.Admin;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.service.AdminService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String wonmoPromise;

    private final AdminService adminService;

    private JwtUtil jwtUtil;

    @Autowired
    public AdminController(AdminService adminService, JwtProperties jwtProperties, JwtUtil jwtUtil){
        this.adminService = adminService;
        this.wonmoPromise = jwtProperties.getAdminWonmo();
        this.jwtUtil = jwtUtil;
    }

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

    @PostMapping("/wonmo")
    public ResponseEntity<Response> addAdmin(@RequestBody Wonmo wonmo){
        Response response = new Response();
        if(wonmo.getWonmoPromise() == this.wonmoPromise) {
            response.setMessage("넌 못들어 간다.");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            String result = adminService.addWonmo(wonmo);
            response.setMessage(result);
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @PostMapping("/wonmo/login")
    public ResponseEntity<Response> loginAdmin(@RequestBody Wonmo wonmo){
        Response res = new Response();
        Admin admin = adminService.logginWonmo(wonmo);
        if(admin != null){
            String jwt = jwtUtil.createAdminToken(admin);
            res.setSuccess(true);
            res.setMessage("OK OK");
            res.setData(jwt);
            return new ResponseEntity<Response>(
                   res, HttpStatus.OK);
        }else{
            return new ResponseEntity<Response>(
                    new Response("Not OK",false, null), HttpStatus.BAD_REQUEST);
        }
    }

}
