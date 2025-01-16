package com.example.backend.mail.model.repository;

import com.example.backend.mail.model.response.EmailResponse;
import com.example.backend.mail.model.response.EmailState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailResponseRepository extends JpaRepository<EmailResponse, Long> {

    // 특정 사용자와 state에 따른 이메일 응답 조회 (전체, 저장, 전송)
    @Query("SELECT e FROM EmailResponse e WHERE e.state = :state")
    List<EmailResponse> findByState(@Param("state") EmailState state);

    // 특정 사용자의 모든 메일 응답 조회
    @Query("SELECT e FROM EmailResponse e")
    List<EmailResponse> findAllEmails();
}
