package net.xasquatch.document.service;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ChattingService {

    @Autowired
    private ChattingRoomDao chattingRoomDao;


    public ChattingRoom createChattingRoom(String title, String pwd) {
        ChattingRoom chattingRoom = ChattingRoom.getInstance(title, pwd);
        int resultInt = chattingRoomDao.insertChattingRoom(chattingRoom);
        if (resultInt != 1) log.error("채팅방 생성에러: {}", title);
        return chattingRoom;
    }


}
