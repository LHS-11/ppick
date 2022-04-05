package com.warrenverr.ppick.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectForm {

    @NotEmpty(message = "제목은 필수항목입니다.")
    @Size(max=255)
    private String title;

    @NotEmpty(message = "프로젝트 분야는 필수항목입니다.")
    @Size(max=30)
    private String type;

    @NotEmpty(message = "프로젝트 배포 형태는 필수항목입니다.")
    @Size(max=100)
    private String export;

    private List<String> skill;

    @NotEmpty(message = "프로젝트 활동 지역은 필수항목입니다.")
    @Size(max=100)
    private String area;

    @NotEmpty(message = "프로젝트 설명은 필수항목입니다.")
    private String content;

    @NotEmpty(message = "프로젝트 이미지는 필수항목입니다.")
    private String image;

    private LocalDate projectStartDate;

    private LocalDate projectEndDate;
    


}
