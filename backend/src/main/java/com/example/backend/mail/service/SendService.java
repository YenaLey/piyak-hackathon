package com.example.backend.mail.service;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.util.Properties;
import org.springframework.stereotype.Service;



@Service
public class SendService {

    public void sendEmail(String accessToken, String recipient, String subject) throws MessagingException {
        // SMTP 서버 설정
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 세션 생성
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Gmail SMTP 사용자와 엑세스 토큰 사용
                return new PasswordAuthentication("your-email@gmail.com", accessToken);
            }
        });

        // 이메일 작성
//        Message message = new MimeMessage(session);
//        message.setFrom(new InternetAddress("your-email@gmail.com")); // 보내는 사람
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient)); // 받는 사람
//        message.setSubject(subject); // 제목
//        message.setText(body); // 내용

        // 이메일 전송
//        Transport.send(message);
    }
}