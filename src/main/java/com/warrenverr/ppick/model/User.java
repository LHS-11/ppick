package com.warrenverr.ppick.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @ElementCollection
    private List<String> skill;

    @Column(length = 100, nullable = false)
    private String job;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(length = 30, nullable = false)
    private String detail_category;

    @Column(length = 255)
    private String image;

    @CreatedDate
    private LocalDateTime date;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String agree;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ElementCollection
    private List<String> portfolio;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<UserProject> applyProjectList;

    /*@OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<UserProject> progressProjectList;*/
}