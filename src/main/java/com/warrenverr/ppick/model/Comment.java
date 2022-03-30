package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @ManyToOne
    private Project project;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<ReComment> reCommentList;

}
