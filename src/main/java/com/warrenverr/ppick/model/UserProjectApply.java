package com.warrenverr.ppick.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class UserProjectApply {

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
