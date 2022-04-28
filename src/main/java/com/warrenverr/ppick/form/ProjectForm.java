package com.warrenverr.ppick.form;

import com.warrenverr.ppick.model.Recruit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate projectEndDate;

    @NotEmpty(message = "프로젝트 모집 인원은 필수항목입니다.")
    private List<String> mainTask;

    @NotEmpty(message = "프로젝트 모집 인원은 필수항목입니다.")
    private List<String> subTask;

    @NotEmpty(message = "프로젝트 모집 인원은 필수항목입니다.")
    private List<Integer> recruitment;

}
