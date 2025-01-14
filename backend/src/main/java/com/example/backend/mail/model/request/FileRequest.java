package com.example.backend.mail.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {
    private MultipartFile fileToSend;
}