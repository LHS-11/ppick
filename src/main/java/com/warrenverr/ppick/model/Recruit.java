package com.warrenverr.ppick.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Recruit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @ElementCollection
    private List<String> mainTask;

    @Column(nullable = false)
    @ElementCollection
    private List<String> subTask;

    @Column(nullable = false)
    @ColumnDefault("0")
    @ElementCollection
    private List<Integer> recruitment;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    @JsonBackReference
    private Project project;
}
