package com.example.backend.mail.controller;

import com.example.backend.mail.model.request.EmailRequest;
import com.example.backend.mail.model.request.EmailSendRequest;
import com.example.backend.mail.model.response.EmailResponse;
import com.example.backend.mail.service.ChatGptService;
import com.example.backend.mail.service.GmailService;
import com.example.backend.mail.service.GoogleOAuthService;
import com.example.backend.mail.service.SendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

    private final SendService sendService;

    private final ChatGptService chatGptService;

    private final GoogleOAuthService googleOAuthService;

    private final GmailService gmailService;

    @PostMapping("/generate-mail")
    public ResponseEntity<EmailResponse> generateEmail(@RequestBody EmailRequest emailRequest) {
        EmailResponse response = chatGptService.generateEmail(emailRequest);
        return ResponseEntity.ok(response);
    }

    private final String BASE_URL = "https://www.googleapis.com/gmail/v1/users/";


    private static final String GMAIL_SCOPES = "https://www.googleapis.com/auth/gmail.readonly";

    @GetMapping("/emails")
    public String getEmails(@RequestParam String accessToken) {
//        // 1. OAuth2.0 인증 코드로 액세스 토큰 받기
//        String accessToken = googleOAuthService.getAccessToken(authorizationCode);

        // 2. 액세스 토큰을 이용해 Gmail API에서 최신 이메일 5개 가져오기
        return gmailService.getLatestEmails(accessToken);
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailSendRequest emailSendRequest) {
        try {
            // EmailService를 호출하여 이메일 전송
            sendService.sendEmail(
                    emailSendRequest.getAccessToken(),
                    emailSendRequest.getRecipient(),
                    emailSendRequest.getSubject(),
                    emailSendRequest.getBody()
            );

            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            // 에러 발생 시 상태 코드와 메시지 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }
}