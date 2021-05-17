package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.ChattingMapper;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ChattingRoomDao {

    @Autowired
    private ChattingMapper chattingMapper;

    public List<ChattingRoom> selectChattingRoomList(String searchValue) {
        return chattingMapper.selectChattingRoomList(searchValue);

    }

    public ChattingRoom selectChattingRoom(Object roomNo) {
        return chattingMapper.selectChattingRoom(roomNo);

    }

    public int insertChattingRoom(ChattingRoom chattingRoom) {
        return chattingMapper.insertChattingRoom(chattingRoom);

    }

    public int updateChattingRoom(ChattingRoom room){
        return updateChattingRoom(room);
    }

    public int deleteChattingRoom(ChattingRoom room){
        return chattingMapper.deleteChattingRoom(room);
    }

    public List<Message> selectMessageList(Object roomNo){
        return chattingMapper.selectMessageList(roomNo);
    }

    public int insertMessage(Message message){
        return chattingMapper.insertMessage(message);
    }

}
