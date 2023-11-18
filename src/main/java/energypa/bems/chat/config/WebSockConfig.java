package energypa.bems.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker
@Configuration
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 메시지 구독 요청의 prefix 설정
        registry.enableSimpleBroker("/sub");

        // 메시지 발행 요청의 prefix 설정
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        // stomp websocket 서버의 endpoint 설정
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*");
    }
}