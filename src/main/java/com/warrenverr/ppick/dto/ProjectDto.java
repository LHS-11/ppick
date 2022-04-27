package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.model.Recruit;
import com.warrenverr.ppick.model.UserProject;
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
    private List<Recruit> recruitList;
    private List<UserProject> applyList;
    private List<UserProject> projectMember;
}
