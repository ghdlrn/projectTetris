package tetris.projectt.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration  //웹소켓 구성 설정
@EnableWebSocketMessageBroker   //스프링 설정 정보 제공
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {  //메세지브로커 설정 정의
        config.setApplicationDestinationPrefixes("/app");
        //애플리케이션에서 처리할 메시지의 접두사 설정 클라이언트가 이 접두사를 사용하여 메시지를 보내면, 해당 메시지는 애플리케이션의 @MessageMapping이나 @SubscribeMapping으로 매핑된 메서드로 라우팅
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {        //클라이언트가 웹소켓 서버에 연결할 수 있는 STOMP엔드포인트 등록
        registry
                .addEndpoint("/ws")     // /ws경로로 웹소켓 연결을 위한 엔드포인트 추가
                .setAllowedOriginPatterns("*")      //모든 도메인에서 크로스 오리진 요청 허용
                .withSockJS();          //SockJS를 사용하여 웹소켓을 지원하지 않는 브라우저도 연결가능하게 함
    }
}