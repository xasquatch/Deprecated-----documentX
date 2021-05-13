package net.xasquatch.document.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketHandlerService extends TextWebSocketHandler {

    private final ChattingRoomDao chattingRoomDao;
    private final ObjectMapper objectMapper;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage txtMessage) throws Exception {
        String msg = txtMessage.getPayload();
        log.info("메세지 전송 = {} : {}", session, msg);
        Message message = objectMapper.readValue(msg, Message.class);
        ChattingRoom chatRoom = chattingRoomDao.selectChattingRoom(message.getNo());
        chatRoom.handleMessage(session, message, objectMapper);
    }


}
