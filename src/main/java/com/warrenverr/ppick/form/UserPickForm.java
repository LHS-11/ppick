package com.warrenverr.ppick.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserPickForm {

    @NotEmpty(message = "전송 내용 필수항목입니다.")
    private String content;
}
