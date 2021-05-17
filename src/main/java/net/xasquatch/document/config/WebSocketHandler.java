package net.xasquatch.document.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChattingRoomDao chattingRoomDao;
    private Map<Long, ChattingRoom> chatRoomSet = new HashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage txtMessage) throws Exception {
        String msg = txtMessage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(msg, Message.class);

        long targetRoomNumber = message.getChatting_room_no();
        ChattingRoom chatRoom = chattingRoomDao.selectChattingRoom(targetRoomNumber);

        if (!chatRoomSet.keySet().contains(targetRoomNumber))
            chatRoomSet.put(targetRoomNumber, chatRoom);

        chatRoomSet.get(targetRoomNumber).handleMessage(session, message, objectMapper);
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error(session.getId() + " 익셉션 발생: " + exception.getMessage());
    }
}
