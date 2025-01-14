package com.example.backend.mail.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleOAuthService {

    private final String CLIENT_ID = "465363714590-fdj341f8gn053o407767b6g0n0toohrp.apps.googleusercontent.com";
    private final String CLIENT_SECRET = null;
    private final String REDIRECT_URI = "http://localhost:8080/auth/google/callback";

    public String getAccessToken(String authorizationCode) {
        // 1. 토큰 요청을 위한 URL
        String tokenUrl = "https://oauth2.googleapis.com/token";

        // 2. 요청할 파라미터 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("grant_type", "authorization_code");

        // 3. RestTemplate을 사용하여 POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, requestEntity, String.class);

        // 4. 액세스 토큰 추출 (응답에서 액세스 토큰을 JSON 파싱)
        String accessToken = extractAccessToken(response.getBody());
        return accessToken;
    }

    // JSON 응답에서 액세스 토큰 추출하는 메소드
    private String extractAccessToken(String responseBody) {
        // 간단히 액세스 토큰만 추출하는 코드 (더 좋은 방법은 JSON 파싱 라이브러리 사용)
        String token = responseBody.split("\"access_token\":\"")[1].split("\"")[0];
        return token;
    }
}