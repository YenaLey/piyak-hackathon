package com.example.backend.mail.model.response;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class EmailResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generated_title", nullable = false, length = 255)
    private String generatedTitle;

    @Column(name = "generated_content", nullable = false, columnDefinition = "TEXT")
    private String generatedContent;

    @Column(name = "recipient_mail", nullable = false, length = 255)
    private String recipientMail;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EmailState state;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}