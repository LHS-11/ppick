package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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

    @Column(length = 255, nullable = false)
    private String title;

    @Column(length = 30, nullable = false)
    private String type;

    @Column(length = 100, nullable = false)
    private String export;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String skill;

    @Column(length = 100, nullable = false)
    private String area;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 255)
    private String image;

    @Column(length = 50, nullable = false)
    private String projectDate;

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime modifyDate;

    @ColumnDefault("0")
    private int views;

    @ManyToMany
    private Set<User> likes;

    @ManyToOne
    private User author;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project", cascade = CascadeType.ALL)
    private List<Comment> commentList;

}
