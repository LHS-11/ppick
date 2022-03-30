package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.ReComment;
import com.warrenverr.ppick.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDto {
    private Integer id;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private Project project;
    private User author;
    private List<ReComment> reCommentList;
}
