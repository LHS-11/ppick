package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.dto.UserProjectApplyDto;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.model.UserProjectApply;
import com.warrenverr.ppick.repository.UserProjectApplyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProjectApplyService {

    private final UserProjectApplyRepository userProjectApplyRepository;

    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }
    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }
    private UserProjectApplyDto of(UserProjectApply userProjectApply) { return modelMapper.map(userProjectApply, UserProjectApplyDto.class); }
    private UserProjectApply of(UserProjectApplyDto userProjectApplyDto) { return modelMapper.map(userProjectApplyDto, UserProjectApply.class); }

    public void create(ProjectDto projectDto, UserDto userDto) {
        UserProjectApplyDto userProjectApplyDto = new UserProjectApplyDto();
        userProjectApplyDto.setProject(projectDto);
        userProjectApplyDto.setUser(userDto);
        this.userProjectApplyRepository.save(of(userProjectApplyDto));
    }
}
