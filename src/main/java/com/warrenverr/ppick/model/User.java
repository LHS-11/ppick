package com.warrenverr.ppick.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true)
    private String sns_id;

    @Column(length = 255, unique = true)
    private String email;

    @Column(length = 100, unique = true)
    private String nickname;

    @Column(columnDefinition = "TEXT")
    private String skill;

    @Column(length = 100)
    private String job;

    @Column(length = 15)
    @CreatedDate
    private String date;

    @Column(columnDefinition = "TEXT")
    private String agree;

}