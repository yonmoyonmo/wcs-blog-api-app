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
import wonmocyberschool.wcsblogapi.entity.Notification;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.payload.Wonmo;
import wonmocyberschool.wcsblogapi.service.AdminService;
import wonmocyberschool.wcsblogapi.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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

    //-------------------------wonmo-------------------
    @PostMapping("/wonmo")
    public ResponseEntity<Response> addAdmin(@RequestBody Wonmo wonmo){
        Response response = new Response();
        if(wonmo.getWonmoPromise() == null){
            response.setMessage("넌 못들어 간다.");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
        if(!wonmo.getWonmoPromise().equals(this.wonmoPromise)) {
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


    //---------------notification----------------------
    @PostMapping("/notification")
    public ResponseEntity<Response> createNotification(HttpServletRequest request,
                                                       @RequestBody Notification notification){
        Response response = new Response();
        String token = request.getHeader("Authorization");
        String email = jwtUtil.getUserEmailFromToken(token);
        if(adminService.saveNotification(email, notification)){
            response.setMessage("new notification is created");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("notification creation failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/notification")
    public ResponseEntity<Response> updateNotification(@RequestBody Notification notification){
        Response response = new Response();
        if(adminService.updateNotification(notification)) {
            response.setMessage("notification updated");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("something went wrong check something me in the future");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/notification")
    public ResponseEntity<Response> deleteNotification(@RequestBody Notification notification){
        Response response = new Response();
        if(adminService.deleteNotification(notification)) {
            response.setMessage("notification deleted");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("시발");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/notification/list")
    public ResponseEntity<Response> getNotificationList(){
        Response response = new Response();
        List<Notification> notifications = adminService.getNotificationList();
        if(notifications.isEmpty()){
            response.setMessage("there's no notification");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.NOT_FOUND);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(notifications);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }


    //---------------category------------------------

    @PostMapping("/category")
    public ResponseEntity<Response> addCategory(HttpServletRequest request,
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

    @PutMapping("/category")
    public ResponseEntity<Response> updateCategory(HttpServletRequest request,
            @RequestBody Category category){
        Response response = new Response();
        if(adminService.updateCategory(category)) {
            response.setMessage("category updated");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("something went wrong check something me in the future");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/category")
    public ResponseEntity<Response> deleteCategory(@RequestBody Category category){
        Response response = new Response();
        if(adminService.deleteCategory(category)) {
            response.setMessage("category deleted");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("조땜");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/public/category/list")
    public ResponseEntity<Response> getCategoryList(){
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
