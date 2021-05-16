package net.xasquatch.document.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
@Service
public class ChattingService {

    @Inject
    private ChattingRoomDao chattingRoomDao;
    private Map<Long, Set<WebSocketSession>> webSocketSessionMap = new HashMap<>();

    public ChattingRoom createChattingRoom(long memberNo, String title, String pwd) {
        ChattingRoom chattingRoom = ChattingRoom.getInstance(title, pwd);
        chattingRoom.setNo(memberNo);
        int resultInt = chattingRoomDao.insertChattingRoom(chattingRoom);
        if (resultInt != 1) log.error("채팅방 생성에러: {}", title);
        return chattingRoom;
    }


    public List<ChattingRoom> getChattingRoomList() {
        List<ChattingRoom> chattingRooms = chattingRoomDao.selectChattingRoomList();
        for (ChattingRoom chattingRoom : chattingRooms) {
            for (Map.Entry<Long, Set<WebSocketSession>> entry : webSocketSessionMap.entrySet()) {
                if (entry.getKey() == chattingRoom.getNo())
                    chattingRoom.setSessions(entry.getValue());
            }
        }

        return chattingRooms;

    }

    public ChattingRoom getChattingRoom(String roomNo) {
        return chattingRoomDao.selectChattingRoom(roomNo);
    }

    public boolean createMessage(Message message){
//        chattingRoomDao.selectMessageList(roomNo);
//        chattingRoomDao.deleteChattingRoom(room);
//        chattingRoomDao.updateChattingRoom(room);

        return chattingRoomDao.insertMessage(message) == 1? true:false;
    }


}
