package com.example.backend.user.controller;


import com.example.backend.user.model.entity.SocialLoginType;
import com.example.backend.user.model.entity.User;
import com.example.backend.user.model.response.UserResponse;
import com.example.backend.user.service.OauthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Log4j2
@Tag(name = "OAuth", description = "소셜 로그인 인증을 시작하고 콜백을 처리하는 API를 제공합니다.")
public class OauthController {

    private final OauthService oauthService;

    @Operation(
            summary = "소셜 로그인 프로세스 시작",
            description = "이 API는 사용자를 소셜 로그인 페이지로 리다이렉트하여 인증 절차를 시작합니다.",
            operationId = "startSocialLogin",
            parameters = {
                    @Parameter(name = "socialLoginType", description = "소셜 로그인 유형 (예: Kakao, Google 등).", required = true)
            })

    @GetMapping(value = "/{socialLoginType}")
    public ResponseEntity<String> socialLoginType(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType) {
        log.info(">> 사용자로부터 SNS 로그인 요청을 받음 :: {} Social Login", socialLoginType);
        String redirectURL = oauthService.request(socialLoginType);
        return ResponseEntity.ok(redirectURL);  // 리다이렉션 URL을 응답으로 반환
    }

    @Operation(
            summary = "소셜 로그인 콜백 처리 (백엔드에서 사용 X)",
            description = "사용자가 소셜 로그인 후 콜백 URL로 받은 코드를 통해 액세스 토큰을 요청합니다.",
            operationId = "handleSocialLoginCallback",
            parameters = {
                    @Parameter(name = "socialLoginType", description = "소셜 로그인 유형 (예: Kakao, Google 등).", required = true),
                    @Parameter(name = "code", description = "소셜 로그인 API 서버로부터 받은 인증 코드.", required = true)
            })
    @GetMapping(value = "/{socialLoginType}/callback")
    public ResponseEntity<?> callback(
            @PathVariable(name = "socialLoginType") SocialLoginType socialLoginType,
            @RequestParam(name = "code") String code,
            HttpServletResponse response) throws IOException {

        System.out.println(socialLoginType);

        log.info(">> 소셜 로그인 API 서버로부터 받은 code :: {}", code);

        // 액세스 토큰을 통해 사용자 정보를 받아온 후 저장
        User user = oauthService.requestAccessTokenAndSaveUser(socialLoginType, code);

        if (user != null) {
            log.info(">> 사용자 정보 DB 저장 완료 :: {}", user.getName());

            // 액세스 토큰을 쿠키에 담을 수도 있습니다 (필요하다면)
            Cookie tokenCookie = new Cookie("accessToken", user.getAccessToken());
            tokenCookie.setPath("/");
            tokenCookie.setHttpOnly(false);
            tokenCookie.setSecure(false);
            tokenCookie.setMaxAge(24 * 3600); // 30분
            response.addCookie(tokenCookie); // 쿠키를 응답에 추가


            // 로그인한 유저 정보를 response body로 반환
//            return ResponseEntity.ok(new UserResponse(user.getName(), user.getAccessToken(), user.getProvider()));
            String redirectUrl = "http://localhost:3000/profile?name=" + user.getName() + "&email=" + user.getEmail();
            response.sendRedirect(redirectUrl);


        } else {
            log.error(">> 사용자 정보 저장 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보 저장 실패");
        }
        return null;
    }

}