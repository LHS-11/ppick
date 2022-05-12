package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String snsid;
    private String email;
    private String nickname;
    private List<String> skill;
    private String job;
    private String category;
    private String detail_category;
    private String image;
    private LocalDateTime date;
    private String agree;
    private UserRole role;
    private List<String> portfolio;
}
