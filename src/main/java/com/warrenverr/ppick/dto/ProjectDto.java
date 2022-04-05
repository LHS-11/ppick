package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProjectDto {
    private Integer id;
    private String title;
    private String type;
    private String export;
    private List<String> skill;
    private String area;
    private String content;
    private String image;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int views;
    private List<UserDto> likes;
    private UserDto author;
    private List<CommentDto> commentList;
}
