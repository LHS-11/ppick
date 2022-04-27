package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String mainTask;

    @Column(nullable = false)
    private String subTask;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int recruitment;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;
}
