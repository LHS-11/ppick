package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<Long> likes;
    private UserDto author;
    private List<CommentDto> commentList;
    private List<RecruitDto> recruitList;
    private List<UserDto> applyList;
    private List<UserDto> projectMember;

    /*public static ProjectDto instance = null;

    public ProjectDto() {
        getInstance();
    }
    public static ProjectDto getInstance() {
        if(instance == null) {
            instance = new ProjectDto();
            return instance;
        }else {
            return instance;
        }
    }*/
}
