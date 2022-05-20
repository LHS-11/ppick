package com.warrenverr.ppick.model;

import com.warrenverr.ppick.role.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String snsid;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 100, unique = true, nullable = false)
    private String nickname;

    @ElementCollection
    private List<String> skill;

    @Column(length = 100, nullable = false)
    private String job;

    @Column(length = 30, nullable = false)
    private String category;

    @Column(length = 30, nullable = false)
    private String detail_category;

    @Column(length = 255)
    private String image;

    @CreatedDate
    private LocalDateTime date;

    @Column(nullable = false)
    private String agree;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ElementCollection
    private List<String> portfolio;

    @Transient
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6";
    }


    @Override
    public String getUsername() {
        return this.snsid;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}