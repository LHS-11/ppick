package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.model.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecruitDto {

    private Integer id;
    private List<String> mainTask;
    private List<String> subTask;
    private List<Integer> recruitment;
}
