package com.warrenverr.ppick.model;

import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String sns_id;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 100, unique = true, nullable = false)
    private String nickname;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String skill;

    @Column(length = 100, nullable = false)
    private String job;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(length = 30, nullable = false)
    private String detail_category;

    @CreatedDate
    private String date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String agree;

    @Enumerated(EnumType.STRING)
    private UserRole role;

}