package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.config.JwtProperties;
import wonmocyberschool.wcsblogapi.entity.Admin;
import wonmocyberschool.wcsblogapi.entity.Category;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.service.AdminService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    //private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String wonmoPromise;

    private final AdminService adminService;

    private JwtUtil jwtUtil;

    @Autowired
    public AdminController(AdminService adminService, JwtProperties jwtProperties, JwtUtil jwtUtil){
        this.adminService = adminService;
        this.wonmoPromise = jwtProperties.getAdminWonmo();
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test/oauth")
    public String testOauth(@RequestParam("token") String token){
        System.out.println("token : " +token);
        return "탁탁탁탁탁";
    }

    //wonmo
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

    //notification 도 만들어야 한다.

    //category
    @PostMapping("/category")
    public ResponseEntity<Response> addCategory(
            HttpServletRequest request,
            @RequestBody Category category){

        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(adminService.createCategory(email, category)){
            response.setMessage("new category is created");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("category creation failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/category/list")
    public ResponseEntity<Response> getCategoryList(
        HttpServletRequest request){

        Response response = new Response();

        List<Category> categories = adminService.readCategoryList();
        if(categories.isEmpty()){
            response.setMessage("there's no categories");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(categories);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

}
