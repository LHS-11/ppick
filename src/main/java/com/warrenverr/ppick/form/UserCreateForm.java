package com.warrenverr.ppick.form;

import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
    private String sns_id;
    private String email;
    private String nickname;
    private String skill;
    private String job;
    private String category;
    private String detail_catrgory;
    private String agree;
    /*private UserRole role;*/
}
