package com.warrenverr.ppick.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProjectApplyDto {

    private Integer id;
    private String field;
    private String motive;
    private LocalDateTime createDate;
    private int status;
    private UserDto userDto;
}
