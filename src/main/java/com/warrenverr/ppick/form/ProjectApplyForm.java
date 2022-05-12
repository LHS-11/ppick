package com.warrenverr.ppick.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProjectApplyForm {

    @NotEmpty(message = "지원 분야는 필수항목입니다.")
    @Size(max=255)
    private String field;

    @NotEmpty(message = "지원 동기는 필수항목입니다.")
    private String motive;
}
