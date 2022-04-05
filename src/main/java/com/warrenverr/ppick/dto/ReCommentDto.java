package com.warrenverr.ppick.dto;

import java.time.LocalDateTime;

public class ReCommentDto {
    private Integer id;
    private UserDto author;
    private CommentDto comment;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
