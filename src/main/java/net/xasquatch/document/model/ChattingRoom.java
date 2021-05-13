package net.xasquatch.document.model;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private ChattingRoom() {
    }

    public static ChattingRoom getInstance(String name, String pwd) {
        ChattingRoom chatRoom = new ChattingRoom();
        chatRoom.name = name;
        chatRoom.pwd = pwd;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, Message message,
                              ObjectMapper objectMapper) throws IOException {

        switch (message.getMessageType()) {
            case ENTER:
                sessions.add(session);
                message.setContents(message.getMbr_nick_name() + "님이 입장하였습니다.");
                message.setMbr_nick_name(null);
                break;

            case LEAVE:
                sessions.remove(session);
                message.setContents(message.getMbr_nick_name() + "님이 퇴장하셨습니다.");
                message.setMbr_nick_name(null);
                break;

            default:
                message.setContents(message.getContents());
                break;

        }
        send(message, objectMapper);
    }

    private void send(Message chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
        for (WebSocketSession session : sessions) session.sendMessage(textMessage);

    }
}
