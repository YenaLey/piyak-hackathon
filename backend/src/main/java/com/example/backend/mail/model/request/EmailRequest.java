package com.example.backend.mail.model.request;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class EmailRequest {
    private Form1 form1;
    private Form2 form2;
    private Form3 form3;

    @Data
    @Setter
    public static class Form1 {
        private String name;           // 내 이름
        private String job;            // 내 직업
        private String affiliation;    // 내 소속
        private String number;         // 내 학번
    }

    @Data
    @Setter
    public static class Form2 {
        private String recipientName;  // 보낼 사람 이름
        private String recipientMail;  // 보낼 이메일
        private String fileToSend;     // 보낼 파일
    }

    @Data
    @Setter
    public static class Form3 {
        private String situation;      // 상황
        private String desiredAnswer;  // 원하는 답변
        private String tone;           // 말투
    }
}
