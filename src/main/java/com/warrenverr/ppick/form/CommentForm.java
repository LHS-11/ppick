package com.warrenverr.ppick.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CommentForm {

    @NotEmpty(message = "댓글은 필수항목입니다.")
    @Size(max=500)
    private String content;
}
