package com.example.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(servers()); // HTTPS 서버 설정 추가
    }

    private Info apiInfo() {
        return new Info()
                .title("API Test") // API의 제목
                .description("Let's practice Swagger UI") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }

    private List<Server> servers() {
        Server httpsServer = new Server()
                .url("https://ffd6-210-94-220-228.ngrok-free.app") // HTTPS URL로 설정
                .description("ngrok 서버"); // 설명 추가

        // 필요한 경우 다른 환경 (예: 테스트 서버) 추가 가능
        return List.of(httpsServer);
    }
}