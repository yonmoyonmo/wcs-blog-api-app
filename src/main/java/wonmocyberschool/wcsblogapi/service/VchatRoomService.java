package wonmocyberschool.wcsblogapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wonmocyberschool.wcsblogapi.entity.VchatRoom;
import wonmocyberschool.wcsblogapi.repository.VchatRoomRepository;

import java.util.List;

@Service
@Transactional
public class VchatRoomService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final VchatRoomRepository vchatRoomRepository;

    @Autowired
    public VchatRoomService(VchatRoomRepository vchatRoomRepository){
        this.vchatRoomRepository = vchatRoomRepository;
    }

    public boolean saveRoom(VchatRoom vchatRoom) {
        if(vchatRoom.getRoomName() == null || vchatRoom.getRoomLink() == null){
            logger.info("bad info");
            return false;
        }
        try {
            vchatRoomRepository.save(vchatRoom);
            return true;
        }catch (Exception e){
            logger.info("while making room...");
            logger.info(e.getMessage());
            return false;
        }
    }

    public boolean deleteRoom(Long id){
        if(id == null){
            logger.info("bad info");
            return false;
        }
        try{
            vchatRoomRepository.deleteById(id);
            return true;
        }catch (Exception e){
            logger.info("while deleting room...");
            logger.info(e.getMessage());
            return false;
        }
    }

    public List<VchatRoom> getRooms() {
        try {
            return vchatRoomRepository.findAll();
        }catch (Exception e) {
            logger.info("while finding rooms...");
            logger.info(e.getMessage());
            return null;
        }
    }
}
