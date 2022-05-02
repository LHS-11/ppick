package com.warrenverr.ppick.service;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }
    private UserDto of(UserCreateForm userCreateForm) { return this.modelMapper.map(userCreateForm, UserDto.class); }

    public UserDto signup(UserCreateForm userCreateForm) {
        UserDto userDto = new UserDto();
        userDto=of(userCreateForm);
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

    public UserDto modify(UserDto userDto, UserCreateForm userCreateForm) {
        userDto.setSkill(userCreateForm.getSkill());
        userDto.setJob(userCreateForm.getJob());
        userDto.setCategory(userCreateForm.getCategory());
        userDto.setDetail_category(userCreateForm.getDetail_category());
        userDto.setAgree(userCreateForm.getAgree());
        User user = of(userDto);
        this.userRepository.save(user);
        return userDto;
    }

    public void delete(UserDto userDto) { this.userRepository.delete(of(userDto));}

}
