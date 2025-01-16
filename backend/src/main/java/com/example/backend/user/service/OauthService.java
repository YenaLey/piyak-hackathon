package com.example.backend.user.service;

import com.example.backend.user.model.entity.SocialLoginType;
import com.example.backend.user.model.entity.User;
import com.example.backend.user.model.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OauthService {

    private final List<SocialOauth> socialOauthList; // 다양한 소셜 로그인 OAuth 처리 객체
    private final UserRepository userRepository; // 사용자 정보를 저장하는 레포지토리

    // 소셜 로그인 요청 URL을 반환하는 메서드
    public String request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(
                socialLoginType); // 주어진 소셜 로그인 타입에 맞는 OAuth 객체 찾기
        return socialOauth.getOauthRedirectURL(); // 해당 OAuth 객체의 리디렉션 URL 반환
    }

    // 인증 코드로 액세스 토큰을 요청하는 메서드
    public String requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(
                socialLoginType); // 주어진 소셜 로그인 타입에 맞는 OAuth 객체 찾기
        return socialOauth.requestAccessToken(code); // 액세스 토큰 요청
    }


    // JSON에서 액세스 토큰만 추출하는 메서드
    private String extractAccessTokenFromJson(String accessTokenJson) {
        // JSON을 파싱하여 액세스 토큰만 추출
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(accessTokenJson);
            // 여기서 필요한 토큰만 반환
            return jsonNode.get("access_token") != null ? jsonNode.get("access_token").asText()
                    : null;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null; // 실패할 경우 null 반환
        }
    }

    // 액세스 토큰을 사용하여 사용자 정보를 가져오고, 사용자 정보가 있으면 저장하는 메서드
    public User requestAccessTokenAndSaveUser(SocialLoginType socialLoginType, String code) {
        // 1. 액세스 토큰을 포함한 JSON 응답을 요청
        String accessTokenJson = this.requestAccessToken(socialLoginType, code);
//        log.info("requestAccessTokenAndSaveUser: " + accessTokenJson);

        // 2. JSON에서 액세스 토큰만 추출
        String accessToken = extractAccessTokenFromJson(accessTokenJson);

        if (accessToken == null) {
            // 액세스 토큰이 없으면 예외 처리
            throw new RuntimeException("Failed to extract access token.");
        }

        // 3. 액세스 토큰을 사용해 사용자 정보 요청
        String userInfo = getUserInfo(socialLoginType, accessToken);

        // 4. 사용자 정보를 파싱하여 User 객체 생성
        User user = parseUserInfo(userInfo, socialLoginType, accessToken);

        // 5. 기존 사용자 확인 후 처리
        Optional<User> existingUser = userRepository.findBySocialId(user.getSocialId());
        if (existingUser.isPresent()) {
            // 이미 존재하는 사용자라면 로그인 처리 (액세스 토큰 갱신 등)
            User existing = existingUser.get();
            existing.setAccessToken(user.getAccessToken()); // 토큰 갱신
            existing.setUpdatedAt(new Timestamp(System.currentTimeMillis())); // 수정 시간 갱신
            return userRepository.save(existing); // 수정된 사용자 정보 저장
        } else {
            // 새 사용자라면 저장
            user.setCreatedAt(new Timestamp(System.currentTimeMillis())); // 생성 시간 설정
            return userRepository.save(user); // 새 사용자 정보 저장
        }
    }

    // 실제 소셜 로그인 API에서 사용자 정보를 받아오는 메서드 (Google, Kakao, Naver 등)
    private String getUserInfo(SocialLoginType socialLoginType, String accessToken) {
        switch (socialLoginType) {
            case GOOGLE:
                return googleApiCall(accessToken);  // Google API 호출 메서드
            default:
                throw new IllegalArgumentException("지원되지 않는 소셜 로그인 타입입니다."); // 지원되지 않는 로그인 타입 오류
        }
    }

    // 각 소셜 로그인 제공자별 API 호출 (실제 API 호출 방식은 각 로그인 서비스의 문서를 참조해야 함)
    // 구글 API 호출 시 응답 상태 코드와 메시지 출력
    public String googleApiCall(String accessToken) {
        try {
            // accessToken을 URL 인코딩
            String encodedAccessToken = URLEncoder.encode(accessToken, "UTF-8");

            log.debug("Encoded access token: {}", encodedAccessToken);
            String url = "https://www.googleapis.com/oauth2/v3/userinfo?access_token="
                    + encodedAccessToken;
            log.debug("Google API URL: {}", url);

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            int responseCode = con.getResponseCode();
            log.info("Google API response code: {}", responseCode);

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                log.info("Successfully received response from Google API.");
                return response.toString();
            } else {
                // 실패 시 에러 메시지와 상태 코드 출력
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer errorResponse = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                in.close();
                log.error("Google API call failed with response code: {}, error: {}", responseCode,
                        errorResponse.toString());
                throw new RuntimeException(
                        "Google API에서 사용자 정보를 가져오는 데 실패했습니다. 응답 코드: " + responseCode + ", 에러 메시지: "
                                + errorResponse.toString());
            }
        } catch (IOException e) {
            log.error("Google API 호출 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("Google API 호출 중 오류 발생", e);
        }
    }


    // 사용자 정보를 파싱하여 User 객체 생성
    private User parseUserInfo(String userInfo, SocialLoginType socialLoginType,
                               String accessToken) {

        JsonObject jsonObject = JsonParser.parseString(userInfo).getAsJsonObject();

        log.info("parseUserInfo :"+ jsonObject);
        // socialId와 name을 소셜 로그인 타입별로 분리
        String socialId = "";
        String name = "";
        String email = "";

        if (socialLoginType == SocialLoginType.GOOGLE) {
            socialId = jsonObject.get("sub").getAsString(); // Google은 "sub"를 ID로 사용
            name = jsonObject.get("name").getAsString();    // Google에서 제공하는 이름
            email = jsonObject.get("email").getAsString();
        }

        // User 객체에 정보 세팅
        User user = new User();
        user.setEmail(email);
        user.setSocialId(socialId);             // 소셜 ID 설정
        user.setName(name);                     // 사용자의 이름 설정
        user.setProvider(socialLoginType.name()); // 로그인 제공자 설정
        user.setAccessToken(accessToken);       // 액세스 토큰 설정
        return user;
    }


    // 주어진 소셜 로그인 타입에 맞는 OAuth 객체를 찾는 메서드
    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "알 수 없는 SocialLoginType 입니다.")); // 지원되지 않는 소셜 로그인 타입 오류
    }
}