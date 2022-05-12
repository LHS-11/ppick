package com.warrenverr.ppick.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ProjectApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String field;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String motive;

    @CreatedDate
    private LocalDateTime createDate;

    //승인 대기 : 0
    //승인 : 1
    @ColumnDefault("0")
    private int status;

    @ManyToOne
    private User user;

}
