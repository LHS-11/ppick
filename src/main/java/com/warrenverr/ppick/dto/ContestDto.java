package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContestDto {

    private String title;
    private String img;
    private String host;
    private String apply_date;
    private String field;
    private String condition;
    private String reward;
    private String content;
    private String link;
    private LocalDateTime createDate;


}
