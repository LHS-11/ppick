package com.warrenverr.ppick.dto;

import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String sns_id;
    private String email;
    private String nickname;
    private List<String> skill;
    private String job;
    private String category;
    private String detail_category;
    private LocalDateTime date;
    private String agree;
    private UserRole role;
}
