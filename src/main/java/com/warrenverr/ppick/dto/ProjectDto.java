package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;
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
    private String skill;
    private String area;
    private String content;
    private String image;
    private String projectDate;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private int views;
    private Set<UserDto> likes;
    private UserDto author;
    private List<CommentDto> commentList;
}
