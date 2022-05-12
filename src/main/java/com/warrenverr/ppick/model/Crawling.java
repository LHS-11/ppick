package com.warrenverr.ppick.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Crawling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String img;

    private String host;

    private String apply_date;

    private String field;

    private String condition;

    private String reward;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String link;
}
