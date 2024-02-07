package com.example.ws_board.chatting;

import com.example.ws_board.chatting.dtos.Message;
import com.example.ws_board.chatting.dtos.ChatRoom;
import com.example.ws_board.chatting.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    // 웹소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String sessionId = session.getId();
        sessions.put(sessionId, session); // session save

        Message message = Message.builder().sender(sessionId).receiver("all").build();
        message.newConnect();

        sessions.values().forEach(s -> {
            try {
                if (!s.getId().equals(sessionId)) {
                    s.sendMessage(new TextMessage(sessionId + "님이 입장 하셨습니다."));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

//        super.afterConnectionEstablished(session);
    }

    // 양방향 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {

        Message message = objectMapper.readValue(textMessage.getPayload(), Message.class);
        message.setSender(session.getId());

        log.info("type : {}", message.getType());
        log.info("sender : {}", message.getSender());
        log.info("receiver : {}", message.getReceiver());
        log.info("data : {}", message.getData());

        WebSocketSession receiver = sessions.get(message.getReceiver()); // 메시지 수신자 찾기

        if (receiver != null && receiver.isOpen()) {
            // 수신자가 존재하고 연결된 상태일 경우 메시지르르 전송한다.
            receiver.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        }
    }

    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO auto-generated method stub
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // 소켓 통신 에러
//    @Override
//    public void handleTransportError(WebSocketSession session) {}
}
