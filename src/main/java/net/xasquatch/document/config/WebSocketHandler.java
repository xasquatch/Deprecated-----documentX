package net.xasquatch.document.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Message;
import net.xasquatch.document.service.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;
import java.util.*;

@Slf4j
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChattingService chattingService;

    private Map<Long, ChattingRoom> chatRoomMap = new HashMap<>();

    private void sessionMapSetting(long targetRoomNumber) {
        try {
            Set<WebSocketSession> sessions = chatRoomMap.get(targetRoomNumber).getSessions();
            chattingService.getWebSocketSessionMap().put(targetRoomNumber, sessions);
        } catch (NullPointerException e) {
            log.info("{}: 해당 채팅룸 없음", targetRoomNumber);
        }

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage txtMessage) throws Exception {
        //메시지 포맷 세팅
        String msg = txtMessage.getPayload();
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(msg, Message.class);
        long targetRoomNumber = message.getChatting_room_no();

        //DB에서 채팅방가져오기
        ChattingRoom chatRoom = chattingService.getChattingRoom(targetRoomNumber);
        ChattingRoom roomDataForMap = null;

        //서버의 Map에 저장되있는 데이터와 대조하여 DB와 일치하게 조작
        if (!chatRoomMap.keySet().contains(targetRoomNumber))
            chatRoomMap.put(targetRoomNumber, chatRoom);

        //새로 입장하는 클라이언트는 이전 메시지내역을 전부 받아온다.
        //(상대방까지 같이 메시지가 출력되는 예외로 일단 보류)
        /*if (message.getMessageType() == MessageType.ENTER) {
            roomDataForMap = chatRoomMap.get(targetRoomNumber);
            List<Message> messageHistory = chattingService.getMessageList(targetRoomNumber);

            for (Message msgHistory : messageHistory)
                roomDataForMap.handleMessage(session, message, objectMapper);

        }*/

        //서버의 Map에 저장되있는 채팅룸 인스턴스 정보를 가져와
        //해당 채팅룸에 메시지를 전송하고
        //채팅룸에 접속한 클라이언트 수(세션사이즈)를 반환 받는다
        roomDataForMap = chatRoomMap.get(targetRoomNumber);
        int targetRoomSessionCount = roomDataForMap.handleMessage(session, message, objectMapper);

        //세션사이즈가 0이면 클라이언트가 없으므로
        //채팅룸 DB에서 enable = 0으로 UPDATE 쿼리를 날린다.
        //서버(WebSocketHandler, ChattingService클래스)의
        //DB관리 맵에서도 해당 룸의 정보를 삭제한다.
        if (targetRoomSessionCount == 0) {
            chattingService.removeChattingRoom(roomDataForMap);
            chattingService.getChattingRoomList().remove(targetRoomNumber);
            chatRoomMap.remove(targetRoomNumber);
        }

        //메시지를 DB로 저장
        chattingService.createMessage(message);

        sessionMapSetting(targetRoomNumber);

        //저장된 클라이언트 정보 모두 내보내기
        Set<WebSocketSession> webSocketSessions = chattingService.getWebSocketSessionMap().get(targetRoomNumber);
        List<Principal> principalList = new ArrayList<>();
        for (WebSocketSession webSocketSession : webSocketSessions)
            principalList.add(webSocketSession.getPrincipal());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(principalList)));


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
