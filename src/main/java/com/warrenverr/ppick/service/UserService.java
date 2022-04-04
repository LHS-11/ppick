package com.warrenverr.ppick.service;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;


    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }

    public UserDto signup(UserCreateForm userCreateForm) {
        UserDto userDto = new UserDto();
        userDto.setSns_id(userCreateForm.getSns_id());
        userDto.setEmail(userCreateForm.getEmail());
        userDto.setNickname(userCreateForm.getNickname());
        userDto.setSkill(userCreateForm.getSkill());
        userDto.setJob(userCreateForm.getJob());
        userDto.setCategory(userCreateForm.getCategory());
        userDto.setDetail_category(userCreateForm.getDetail_catrgory());
        userDto.setAgree(userCreateForm.getAgree());
        User user = of(userDto);
        this.userRepository.save(user);

        return userDto;
    }

    public UserDto loginByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            return of(user.get());
        }else {
            throw new DataNotFoundException("project not found");
        }
    }



}
