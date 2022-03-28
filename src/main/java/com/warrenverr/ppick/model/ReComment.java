package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class ReComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Comment c_id;

    @ManyToOne
    private User author;

    @ManyToOne
    private Comment comment;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

}