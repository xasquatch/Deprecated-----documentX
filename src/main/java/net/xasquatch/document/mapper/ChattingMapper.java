package net.xasquatch.document.mapper;

import net.xasquatch.document.mapper.builder.ChattingBuilder;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface ChattingMapper {

    //    @Select("SELECT no, name, pwd, " +
//            "DATE_FORMAT(date, '%Y.%m.%d %H:%i:%s') AS date " +
//            "FROM chatting_room WHERE enable = 1")
    @SelectProvider(type = ChattingBuilder.class, method = "selectChattingRoomList")
    List<ChattingRoom> selectChattingRoomList(Object searchValue);

    @Select("SELECT * FROM chatting_room WHERE enable = 1 AND no = #{arg0}")
    ChattingRoom selectChattingRoom(Object roomNo);

    @InsertProvider(type = ChattingBuilder.class, method = "insertChattingRoom")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "no", before = false, resultType = long.class)
    int insertChattingRoom(ChattingRoom room);

    @Update("UPDATE chatting_room SET name=#{name}, pwd=#{pwd} WEHRE no = #{no}")
    int updateChattingRoom(ChattingRoom room);

    @Update("UPDATE chatting_room SET enable=0 WHERE no = #{no}")
    int deleteChattingRoom(ChattingRoom room);

    @SelectProvider(type = ChattingBuilder.class, method = "selectMessageList")
    List<Message> selectMessageList(Object roomNo);

    @Insert("INSERT INTO msg(mbr_no, chatting_room_no, contents, ip_address) " +
            "VALUES(#{mbr_no},#{chatting_room_no},#{contents},#{ip_address})")
    int insertMessage(Message message);

    @Select("SELECT c.no, c.name, mbr.no AS mbr_no, c.date " +
            "FROM mbr " +
            "RIGHT OUTER JOIN msg ON mbr.no = msg.mbr_no " +
            "LEFT OUTER JOIN chatting_room c ON c.no = msg.chatting_room_no " +
            "GROUP BY c.name " +
            "HAVING mbr.no = #{arg0} " +
            "ORDER BY c.no DESC " +
            "LIMIT #{arg1} , #{arg2}")
    List<Map<String, Object>> selectChatHistoryList(Object memberNo, Object currentPage, Object limit);

    @Select("SELECT convert(msg.contents USING UTF8) AS contents, msg.date, c.name " +
            "FROM mbr " +
            "RIGHT OUTER JOIN msg ON mbr.no = msg.mbr_no " +
            "LEFT OUTER JOIN chatting_room c ON c.no = msg.chatting_room_no " +
            "WHERE mbr.no = #{arg0} AND c.no = #{arg1} " +
            "ORDER BY msg.no DESC")
    List<Map<String, Object>> selectChatHistory(Object memberNo, Object roomNo);
}
