package net.xasquatch.document.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRoomDao chattingRoomDao;


    public ChattingRoom createChattingRoom(String title, String pwd) {
        ChattingRoom chattingRoom = ChattingRoom.getInstance(title, pwd);
        int resultInt = chattingRoomDao.insertChattingRoom(chattingRoom);
        if (resultInt != 1) log.error("채팅방 생성에러: {}", title);
        return chattingRoom;
    }


    public List<ChattingRoom> getChattingRoomList() {
        return chattingRoomDao.selectChattingRoomList();

    }

    public ChattingRoom getChattingRoom(String roomNo) {
        return chattingRoomDao.selectChattingRoom(roomNo);
    }
}
