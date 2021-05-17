package net.xasquatch.document.mapper.builder;

import net.xasquatch.document.model.ChattingRoom;
import org.apache.ibatis.jdbc.SQL;

public class ChattingBuilder {

    public static final String insertChattingRoom(ChattingRoom room) {
        return new SQL() {{
            INSERT_INTO("chatting_room");
            INTO_COLUMNS("name", "pwd", "enable");
            INTO_VALUES("'" + room.getName() + "'",
                    "'" + room.getPwd() + "'", "1");

        }}.toString();
    }

    public static final String selectMessageList(Object roomNo) {
        return new SQL() {{
            SELECT("ms.no AS no, " +
                    "ms.mbr_no AS mbr_no, " +
                    "m.nick_name AS mbr_nick_name, " +
                    "ms.chatting_room_no AS chatting_room_no, " +
                    "convert(ms.contents USING UTF8) AS contents, " +
                    "DATE_FORMAT(ms.date, '%Y.%m.%d %H:%i:%s') AS date, " +
                    "REPLACE(ms.ip_address, RIGHT(ms.ip_address, 4),'.***') AS ip_address");
            FROM("msg ms");
            LEFT_OUTER_JOIN("mbr m ON m.no = ms.mbr_no");
            WHERE("chatting_room_no = " + roomNo);
        }}.toString();
    }
}