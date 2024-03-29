package tetris.projectt.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration      //스프링의 설정정보 제공
@EnableWebMvc   //SpringMVC활성화하여 웹 애플리케이션 사용 준비가 되었음을 알림
public class WebConfig implements WebMvcConfigurer {        //Spring MVC 웹 설정 커스터마이징
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {     //정적리소스(html,css,javascript 파일등)에대한 요청을 처리하기 위한 리소스 핸들러 추가
        registry.addResourceHandler("/**")      //  "/**"는 모든 경로에 대한 요청이 들어올 때 사용할 리소스 핸들러를 설정
                .addResourceLocations("classpath:/static/");    //정적 리소스가 위치한 디렉터리 지정   (/static/ 디렉터리 안)
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {        //CORS정책을 설정, 웹페이지가 다른 도메인의 리소스를 요청할 수 있도록 허용하는 메커니즘
        registry.addMapping("/**").allowedOriginPatterns("*");      //모든경로(/**)에 대한 요청에 대해 모든 도메인(*)의 접근 허용(개발환경에서 유용할 수 있으나 보안상의 이유로 실제 프로덕션 환경에서는 특정 도메인에 대해서만 접근 허용하도록 제한)
    }
}