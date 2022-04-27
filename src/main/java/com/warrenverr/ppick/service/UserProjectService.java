package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.model.UserProject;
import com.warrenverr.ppick.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProjectService {

    private final UserProjectRepository userProjectRepository;

    private final ModelMapper modelMapper;

    private UserDto of(User user) { return this.modelMapper.map(user, UserDto.class); }
    private User of(UserDto userDto) { return this.modelMapper.map(userDto, User.class); }
    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }

    public void create(ProjectDto projectDto, UserDto userDto) {
        UserProject userProject = new UserProject();
        Project project = of(projectDto);
        User user = of(userDto);
        userProject.setProject(project);
        userProject.setUser(user);
        this.userProjectRepository.save(userProject);
    }
}
