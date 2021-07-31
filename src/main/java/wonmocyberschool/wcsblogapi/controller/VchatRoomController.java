package wonmocyberschool.wcsblogapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wonmocyberschool.wcsblogapi.entity.VchatRoom;
import wonmocyberschool.wcsblogapi.payload.Response;
import wonmocyberschool.wcsblogapi.service.VchatRoomService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class VchatRoomController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VchatRoomService vchatRoomService;

    @Autowired
    public VchatRoomController(VchatRoomService vchatRoomService){
        this.vchatRoomService = vchatRoomService;
    }

    @PostMapping("/public/chat-room")
    public ResponseEntity<Response> createRoom(HttpServletRequest request,
                                               @RequestBody VchatRoom vchatRoom){
        logger.info("at : /public/chat-room : POST");
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        logger.info("creating room... "+ clientIp);

        Response response = new Response();
        if(!vchatRoomService.saveRoom(vchatRoom)){
            response.setMessage("failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @DeleteMapping("/public/chat-room")
    public ResponseEntity<Response> deleteRoom(HttpServletRequest request,
                                               @RequestBody VchatRoom vchatRoom){
        logger.info("at : /public/chat-room : DELETE");
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        logger.info("deleting room... "+ clientIp);

        Response response = new Response();
        Long roomId = vchatRoom.getId();

        if(!vchatRoomService.deleteRoom(roomId)){
            response.setMessage("failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/public/roomlist")
    public ResponseEntity<Response> getRooms(HttpServletRequest request){
        Response response = new Response();

        List<VchatRoom> roomList = new ArrayList<>();
        roomList = vchatRoomService.getRooms();
        if(roomList == null){
            response.setMessage("failed");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }else if(roomList.isEmpty()){
            response.setMessage("empty list");
            response.setSuccess(false);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }else{
            response.setMessage("OK");
            response.setSuccess(true);
            response.setData(roomList);
            return new ResponseEntity<Response>(response, HttpStatus.OK);
        }
    }
}
