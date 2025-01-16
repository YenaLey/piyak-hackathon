package com.example.backend.mail.service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class SendService {


    private final String GMAIL_API_SEND_URL = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

    public String sendEmail(String accessToken, String recipient, String subject, String body) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        MimeMessage email = createEmail(recipient, "your-email@gmail.com", subject, body);
        String encodedEmail = encodeEmail(email);

        JSONObject emailContent = new JSONObject();
        emailContent.put("raw", encodedEmail);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> requestEntity = new HttpEntity<>(emailContent.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(GMAIL_API_SEND_URL, HttpMethod.POST,
                requestEntity, String.class);

        return response.getBody();
    }


    private MimeMessage createEmail(String to, String from, String subject, String bodyText) {
        try {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage email = new MimeMessage(session);

            email.setFrom(new InternetAddress(from));  // 발신자
            email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to)); // 수신자
            email.setSubject(subject);  // 제목
            email.setText(bodyText);  // 본문

            return email;
        } catch (Exception e) {
            throw new RuntimeException("Error creating email", e);
        }
    }

    private String encodeEmail(MimeMessage email) {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            return Base64.getUrlEncoder().encodeToString(buffer.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding email", e);
        }
    }

}