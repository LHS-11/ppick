package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
    private String date;

    @Column(columnDefinition = "TEXT")
    private String agree;

}