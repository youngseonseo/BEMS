package energypa.bems.chat.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.chat.entity.ChatMessage;
import energypa.bems.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;

    public static List<WebSocketSession> sessionList = new ArrayList<>(); // 애플리케이션 전체에 채팅방이 단 1개임을 가정하고 작성한 코드

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {

        log.info("브라우저와의 websocket 연결이 성공적으로 이뤄졌습니다!");

        sessionList.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        for (WebSocketSession wsSession : WebSocketHandler.sessionList) {

            try {
                wsSession.sendMessage(message);
            }
            catch (IOException e) {
                log.info("WebSocketSession ID가 " + wsSession.getId() + "인 websocket 연결에 오류가 발생하여 해당 유저에게 메시지를 전송하는데 실패했습니다!");
            }

            String payload = message.getPayload();
            try {
                ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
                chatMessageRepository.save(chatMessage);
            }
            catch (JsonProcessingException e) {
                log.info("서버로부터 받은 JSON을 chatMessage로 변환하는데 실패하여 메시지를 DB에 저장하지 못했습니다!");
                // 애플리케이션 관리자에게 알림 주는 코드
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        log.info("브라우저와의 websocket 연결이 종료되었습니다!");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {

        log.info("websocket 연결에 오류가 발생했습니다!");
    }
}
