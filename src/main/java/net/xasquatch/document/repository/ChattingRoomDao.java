package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.ChattingMapper;
import net.xasquatch.document.model.ChattingRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChattingRoomDao {

    @Autowired
    private ChattingMapper chattingMapper;

    public List<ChattingRoom> selectChattingRoomList() {
        return chattingMapper.selectChattingRoomList();

    }

    public ChattingRoom selectChattingRoom(long roomNo) {
        return chattingMapper.selectChattingRoom(roomNo);

    }

    public int insertChattingRoom(ChattingRoom chattingRoom) {
        return chattingMapper.insertChattingRoom(chattingRoom);

    }
}
