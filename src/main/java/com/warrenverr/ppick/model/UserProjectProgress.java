package com.warrenverr.ppick.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class UserProjectProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JsonBackReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Project project;
}
