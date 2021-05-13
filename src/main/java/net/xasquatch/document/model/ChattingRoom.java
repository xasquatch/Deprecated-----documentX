package net.xasquatch.document.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Data
public class ChattingRoom {
    private long no;
    private long masterNo;
    private String name;
    private String pwd;

    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChattingRoom getInstance(long no, String name) {
        ChattingRoom chatRoom = new ChattingRoom();
        chatRoom.no = no;
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, Message message,
                              ObjectMapper objectMapper) throws IOException {

        switch (message.getMessageType()) {
            case ENTER:
                sessions.add(session);
                message.setMbr_nick_name(null);
                message.setContents(message.getMbr_nick_name() + "님이 입장하였습니다.");
                break;

            case LEAVE:
                sessions.remove(session);
                message.setMbr_nick_name(null);
                message.setContents(message.getMbr_nick_name() + "님이 퇴장하셨습니다.");
                break;

            default:
                message.setMbr_nick_name(message.getMbr_nick_name());
                message.setContents(message.getContents());
                break;

        }
        send(message, objectMapper);
    }

    private void send(Message chatMessage, ObjectMapper objectMapper) throws IOException {
        ObjectWriter prettyPrinter = objectMapper.writerWithDefaultPrettyPrinter();
        TextMessage textMessage = new TextMessage(prettyPrinter.writeValueAsString(chatMessage.getContents()));
        for (WebSocketSession session : sessions) session.sendMessage(textMessage);

    }
}
