package com.warrenverr.ppick.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProjectApplyDto {

    private Integer id;
    @JsonBackReference
    private UserDto user;
    @JsonBackReference
    private ProjectDto project;
}
