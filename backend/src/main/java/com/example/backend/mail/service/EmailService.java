package com.example.backend.mail.service;

import com.example.backend.mail.model.repository.EmailResponseRepository;
import com.example.backend.mail.model.response.EmailResponse;
import com.example.backend.mail.model.response.EmailState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailResponseRepository emailResponseRepository;

    // 모든 메일 응답 조회
    public List<EmailResponse> getAllEmails() {
        return emailResponseRepository.findAllEmails();
    }

    // 저장된 메일 응답 조회
    public List<EmailResponse> getSavedEmails() {
        return emailResponseRepository.findByState(EmailState.SAVED);
    }

    // 전송된 메일 응답 조회
    public List<EmailResponse> getSentEmails() {
        return emailResponseRepository.findByState(EmailState.SENT);
    }

    public void updateState(EmailResponse emailResponse) {
        emailResponseRepository.save(emailResponse);
    }
}