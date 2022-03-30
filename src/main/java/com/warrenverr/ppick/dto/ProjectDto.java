package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.model.Comment;
import com.warrenverr.ppick.model.User;
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
    private Set<User> likes;
    private User author;
    private List<Comment> commentList;
}
