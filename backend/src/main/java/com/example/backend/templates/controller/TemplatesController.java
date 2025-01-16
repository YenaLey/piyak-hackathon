package com.example.backend.templates.controller;

import com.example.backend.templates.model.entity.Template;
import com.example.backend.templates.service.TemplatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplatesController {
    private final TemplatesService templatesService;

    @GetMapping("/get-templates")
    public ResponseEntity<List<Template>> getTemplates() {
        return null;
    }
}
