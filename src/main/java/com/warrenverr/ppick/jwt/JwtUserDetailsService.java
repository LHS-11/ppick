package com.warrenverr.ppick.jwt;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    private UserDto of(com.warrenverr.ppick.model.User user) { return this.modelMapper.map(user, UserDto.class); }
    private com.warrenverr.ppick.model.User of(UserDto userDto) { return this.modelMapper.map(userDto, com.warrenverr.ppick.model.User.class); }
    private UserDto of(UserCreateForm userCreateForm) { return this.modelMapper.map(userCreateForm, UserDto.class); }
    private final ModelMapper modelMapper;
    private final UserService userService;

    public JwtUserDetailsService(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String snsid) throws UsernameNotFoundException {

        com.warrenverr.ppick.model.User user =  userService.loginSnsid(snsid);

        return user;
    }

    public UserDto loadUserDto(String snsid) throws UsernameNotFoundException {
        return userService.loginBySnsid(snsid);
    }

}