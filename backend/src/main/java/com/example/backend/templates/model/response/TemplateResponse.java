package com.example.backend.templates.model.response;

import lombok.Data;

@Data
public class TemplateResponse {
    private String title;
    private String content;
    private String date;
    private String recipientName;
}
