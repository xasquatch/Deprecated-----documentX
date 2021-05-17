package net.xasquatch.document.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
public class ChattingRoom {
    private long no;
    private String name;
    private String pwd;
    private String date;

    private long masterNo;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChattingRoom that = (ChattingRoom) o;
        return no == that.no;
    }

    @Override
    public int hashCode() {
        return Objects.hash(no);
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

        }
        send(message, objectMapper);
    }

    private void send(Message chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage));
        for (WebSocketSession session : sessions) session.sendMessage(textMessage);

    }
}
