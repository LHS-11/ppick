package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReCommentDto {
    private Integer id;
    private UserDto author;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
