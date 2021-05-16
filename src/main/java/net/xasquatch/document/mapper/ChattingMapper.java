package net.xasquatch.document.mapper;

import net.xasquatch.document.mapper.builder.ChattingBuilder;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface ChattingMapper {

    @Select("SELECT * FROM chatting_room WHERE enable = 1")
    List<ChattingRoom> selectChattingRoomList();

    @Select("SELECT * FROM chatting_room WHERE enable = 1 AND no = #{arg0}")
    ChattingRoom selectChattingRoom(Object roomNo);

    @InsertProvider(type = ChattingBuilder.class, method = "insertChattingRoom")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "no", before = false, resultType = long.class)
    int insertChattingRoom(ChattingRoom room);

    @Update("UPDATE chatting_room SET name=#{name}, pwd=#{pwd} WEHRE no = #{no}")
    int updateChattingRoom(ChattingRoom room);

    @Update("UPDATE chatting_room SET enable=0 WEHRE no = #{no}")
    int deleteChattingRoom(ChattingRoom room);

    @SelectProvider(type = ChattingBuilder.class, method = "selectMessageList")
    List<Message> selectMessageList(Object roomNo);

    @Insert("INSERT INTO msg(mbr_no, chatting_room_no, contents, ip_address) " +
            "VALUES(#{mbr_no},#{chatting_room_no},#{contents},#{ip_address})")
    int insertMessage(Message message);



}
