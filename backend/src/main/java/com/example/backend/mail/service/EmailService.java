package com.example.backend.mail.service;

import com.example.backend.mail.model.entity.Email;
import com.example.backend.mail.model.repository.EmailRepository;
import com.example.backend.mail.model.repository.EmailResponseRepository;
import com.example.backend.mail.model.response.EmailResponse;
import com.example.backend.mail.model.response.EmailState;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailResponseRepository emailResponseRepository;

    // 모든 메일 응답 조회
    public List<EmailResponse> getAllEmails(String recipientMail) {
        return emailResponseRepository.findAllByRecipientMail(recipientMail);
    }

    // 저장된 메일 응답 조회
    public List<EmailResponse> getSavedEmails(String recipientMail) {
        return emailResponseRepository.findByRecipientMailAndState(recipientMail, EmailState.SAVED);
    }

    // 전송된 메일 응답 조회
    public List<EmailResponse> getSentEmails(String recipientMail) {
        return emailResponseRepository.findByRecipientMailAndState(recipientMail, EmailState.SENT);
    }
}