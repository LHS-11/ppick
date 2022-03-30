package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 255)
    private String title;

    @Column(length = 30)
    private String type;

    @Column(length = 100)
    private String export;

    @Column(columnDefinition = "TEXT")
    private String skill;

    @Column(length = 100)
    private String area;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 255)
    private String image;

    @Column(length = 50)
    private String projectDate;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private int views;

    @ManyToMany
    private Set<User> likes;

    private int valid;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Comment> commentList;
}
