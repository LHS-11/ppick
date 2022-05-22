package com.warrenverr.ppick.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Contest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String img;

    private String host;

    private String apply_date;

    private String field;

//    @CreatedDate
    private LocalDateTime createDate;

    private String condition;

    private String reward;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String link;
}
