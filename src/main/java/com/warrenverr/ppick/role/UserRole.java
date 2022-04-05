package com.warrenverr.ppick.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    //관리자, 비회원, 회원
    ADMIN("USER_ADMIN"), GUEST("USER_GUEST"), MEMBER("USER_MEMBER");


    private String value;
}
