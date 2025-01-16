package com.example.backend.user.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserAffiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String affiliation;
    private String number;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}