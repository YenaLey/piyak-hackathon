package com.example.backend.templates.service;

import com.example.backend.mail.service.ChatGptService;
import com.example.backend.mail.service.EmailService;
import com.example.backend.templates.model.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplatesService {

    private final TemplateRepository templateRepository;
    private final ChatGptService chatGptService;
    private final EmailService emailService;

}
