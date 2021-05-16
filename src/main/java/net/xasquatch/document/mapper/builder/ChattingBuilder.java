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
}