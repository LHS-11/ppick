package com.warrenverr.ppick.service;

import com.warrenverr.ppick.config.DataNotFoundException;
import com.warrenverr.ppick.dto.ProjectApplyDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.RecruitDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.ProjectApply;
import com.warrenverr.ppick.model.Recruit;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.ProjectApplyRepository;
import com.warrenverr.ppick.repository.ProjectRepository;
import com.warrenverr.ppick.repository.RecruitRepository;
import com.warrenverr.ppick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProjectApplyRepository projectApplyRepository;
    private final RecruitRepository recruitRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }
    private UserDto of(UserCreateForm userCreateForm) { return this.modelMapper.map(userCreateForm, UserDto.class); }
    private ProjectApplyDto of(ProjectApply projectApply) { return this.modelMapper.map(projectApply, ProjectApplyDto.class); }
    private ProjectApply of(ProjectApplyDto projectApplyDto) { return this.modelMapper.map(projectApplyDto, ProjectApply.class); }
    private Recruit of(RecruitDto recruitDto) { return modelMapper.map(recruitDto, Recruit.class); }
    private RecruitDto of(Recruit recruit) { return modelMapper.map(recruit, RecruitDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }

    public UserDto signup(UserCreateForm userCreateForm) {
        UserDto userDto = new UserDto();
        userDto=of(userCreateForm);
        User user = of(userDto);
        this.userRepository.save(user);

        return userDto;
    }

    public UserDto loginBySnsid(String snsid) {
        Optional<User> user = this.userRepository.findBySnsid(snsid);
        if(user.isPresent()) {
            return of(user.get());
        }else {
            throw new DataNotFoundException("project not found");
        }
    }

    public User loginSnsid(String snsid) {
        Optional<User> user = this.userRepository.findBySnsid(snsid);
        if(user.isPresent()) {
            return user.get();
        }else {
            throw new DataNotFoundException("project not found");
        }
    }

    public String storeImage(MultipartFile file) {


        return "";
    }

    /*public UserDto loginByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if(user.isPresent()) {
            return of(user.get());
        }else {
            throw new DataNotFoundException("project not found");
        }
    }
*/
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

    private ProjectApplyDto getProjectApplyDto(Integer id) {
        return of(this.projectApplyRepository.findById(id).get());
    }

    public void approve(ProjectDto projectDto, Integer id) {
        ProjectApplyDto projectApplyDto = getProjectApplyDto(id);
        projectApplyDto.setStatus(1);
        ProjectApply projectApply = this.projectApplyRepository.save(of(projectApplyDto));

        List<UserDto> projectMember = projectDto.getProjectMember();
        projectMember.add(of(projectApply).getUserDto());
        projectDto.setProjectMember(projectMember);

        RecruitDto recruitDto = projectDto.getRecruit();
        for(int i=0; i<recruitDto.getSubTask().size(); i++) {
            if(recruitDto.getSubTask().get(i).equals(projectApplyDto.getField())) {
                Integer recruitment = recruitDto.getRecruitment().get(i);
                recruitDto.getRecruitment().set(i,recruitment-1);
            }
        }
        Recruit recruit = this.recruitRepository.save(of(recruitDto));

        projectDto.setRecruit(of(recruit));

        this.projectRepository.save(of(projectDto));

    }
    public List<UserDto> findAllUser() {
        List<User> userList = userRepository.findAll();
        List<UserDto> userDtoList = userList.stream().map(p -> modelMapper.map(p, UserDto.class)).collect(Collectors.toList());
        return userDtoList;
    }

    public UserDto findUserById(Long id) {
        User user = userRepository.findById(id).get();
        return of(user);
    }
}
