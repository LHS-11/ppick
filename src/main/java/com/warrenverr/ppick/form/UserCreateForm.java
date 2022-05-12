package com.warrenverr.ppick.form;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

@Getter
@Setter
public class UserCreateForm {
    private String snsid;
    private String email;
    private String nickname;
    private List<String> skill;
    private String job;
    private String category;
    private String detail_category;
    private String image;
    private String agree;
    private List<String> portfolio;

}
