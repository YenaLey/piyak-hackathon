package com.example.backend.templates.model.repository;

import com.example.backend.templates.model.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}
