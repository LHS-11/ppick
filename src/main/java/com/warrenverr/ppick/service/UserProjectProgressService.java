package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.dto.UserProjectProgressDto;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.model.UserProjectApply;
import com.warrenverr.ppick.model.UserProjectProgress;
import com.warrenverr.ppick.repository.UserProjectApplyRepository;
import com.warrenverr.ppick.repository.UserProjectProgressRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserProjectProgressService {

    private final UserProjectProgressRepository userProjectProgressRepository;
    private final UserProjectApplyRepository userProjectApplyRepository;

    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }
    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }
    private UserProjectProgressDto of(UserProjectProgress userProjectProgress) { return modelMapper.map(userProjectProgress, UserProjectProgressDto.class); }
    private UserProjectProgress of(UserProjectProgressDto userProjectProgressDto) { return modelMapper.map(userProjectProgressDto, UserProjectProgress.class); }

    public void approve(ProjectDto projectDto, UserDto userDto) {
        UserProjectProgressDto userProjectProgressDto = new UserProjectProgressDto();
        userProjectProgressDto.setProject(projectDto);
        userProjectProgressDto.setUser(userDto);
        Optional<UserProjectApply> userProjectApply = this.userProjectApplyRepository.findByUserAndProject(of(userDto),of(projectDto));
        this.userProjectProgressRepository.save(of(userProjectProgressDto));
        this.userProjectApplyRepository.delete(userProjectApply.get());
    }
}
