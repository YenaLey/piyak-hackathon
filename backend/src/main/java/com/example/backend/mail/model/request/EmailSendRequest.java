package com.example.backend.mail.model.request;

import lombok.Data;
import lombok.Setter;

@Setter
@Data
public class EmailSendRequest {

    private String accessToken; // Gmail 액세스 토큰
    private String recipient; // 받는 사람 이메일 주소
    private String subject; // 이메일 제목
    private String body; // 이메일 내용

}
