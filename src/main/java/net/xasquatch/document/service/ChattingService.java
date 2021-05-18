package net.xasquatch.document.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.config.WebSocketHandler;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Getter
@Service
public class ChattingService {

    @Autowired
    private ChattingRoomDao chattingRoomDao;
    @Autowired
    private WebSocketHandler webSocketHandler;

    private Map<Long, Set<WebSocketSession>> webSocketSessionMap = new HashMap<>();

    public ChattingRoom createChattingRoom(long memberNo, String title, String pwd) {
        ChattingRoom chattingRoom = new ChattingRoom();
        chattingRoom.setMasterNo(memberNo);
        chattingRoom.setName(title);
        chattingRoom.setPwd(pwd);

        int resultInt = chattingRoomDao.insertChattingRoom(chattingRoom);
        webSocketHandler.getChatRoomMap().put(chattingRoom.getNo(), chattingRoom);

        if (resultInt != 1) log.error("채팅방 생성에러: {}", title);
        return chattingRoom;
    }

    public List<ChattingRoom> getChattingRoomList(String searchValue) {
        List<ChattingRoom> chattingRooms = chattingRoomDao.selectChattingRoomList(searchValue);
        for (ChattingRoom chattingRoom : chattingRooms) {
            for (Map.Entry<Long, Set<WebSocketSession>> entry : webSocketSessionMap.entrySet()) {
                if (entry.getKey() == chattingRoom.getNo())
                    chattingRoom.setSessions(entry.getValue());
            }
        }

        return chattingRooms;

    }

    public ChattingRoom getChattingRoom(long roomNo) {
        return chattingRoomDao.selectChattingRoom(roomNo);
    }

    public boolean createMessage(Message message) {
        return chattingRoomDao.insertMessage(message) == 1 ? true : false;
    }

    public boolean modifyChattingRoom(ChattingRoom room) {
        return chattingRoomDao.updateChattingRoom(room) == 1 ? true : false;
    }

    public boolean removeChattingRoom(ChattingRoom room) {
        return chattingRoomDao.deleteChattingRoom(room) == 1 ? true : false;
    }

    public List<Message> getMessageList(long roomNo) {
        return chattingRoomDao.selectMessageList(roomNo);
    }

    public List<Map<String,Object>> selectChatHistoryList(long memberNo, int currentPage, int limit){
        return chattingRoomDao.selectChatHistoryList(memberNo, currentPage, limit);
    }
}
