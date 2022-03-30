package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.model.Comment;
import com.warrenverr.ppick.model.User;

import java.time.LocalDateTime;

public class ReCommentDto {
    private Integer id;
    private Comment c_id;
    private User author;
    private Comment comment;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
