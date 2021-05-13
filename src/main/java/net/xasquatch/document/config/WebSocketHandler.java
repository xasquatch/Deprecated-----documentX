package net.xasquatch.document.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChattingRoomDao chattingRoomDao;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage txtMessage) throws Exception {
        String msg = txtMessage.getPayload();
        log.info("메세지 전송 = {} : {}", session, msg);
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(msg, Message.class);
        ChattingRoom chatRoom = chattingRoomDao.selectChattingRoom(message.getNo());
        chatRoom.handleMessage(session, message, objectMapper);
    }
    @Override
    public void handleTransportError(
            WebSocketSession session, Throwable exception) throws Exception {
        log.error(session.getId() + " 익셉션 발생: " + exception.getMessage());
    }

}
