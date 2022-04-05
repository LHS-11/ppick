package com.warrenverr.ppick.service;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.model.Project;

import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private  final ModelMapper modelMapper;

    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }

    //프로젝트 생성
    public ProjectDto create(ProjectForm projectForm, UserDto userDto) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setTitle(projectForm.getTitle());
        projectDto.setType(projectForm.getType());
        projectDto.setExport(projectForm.getExport());
        projectDto.setSkill(projectForm.getSkill());
        projectDto.setArea(projectForm.getArea());
        projectDto.setContent(projectForm.getContent());
        projectDto.setImage(projectForm.getImage());
        projectDto.setProjectStartDate(projectForm.getProjectStartDate());
        projectDto.setProjectEndDate(projectForm.getProjectEndDate());
        projectDto.setAuthor(userDto);
        Project project = of(projectDto);
        this.projectRepository.save(project);
        return projectDto;
    }

    @Transactional
    public ProjectDto getProject(Integer id) {
        Optional<Project> project = this.projectRepository.findById(id);
        if(project.isPresent()) {
            return of(project.get());
        } else {
            throw new DataNotFoundException("project not found");
        }
    }

    @Transactional
    public Page<ProjectDto> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        Specification<Project> specification = search(keyword);
        Page<Project> projectList = this.projectRepository.findAll(specification, pageable);
        Page<ProjectDto> projectDtoList = projectList.map(project -> of(project));
        return projectDtoList;
    }

    //프로젝트 수정
    public ProjectDto modify(ProjectDto projectDto, ProjectForm modifyProject) {
        projectDto.setTitle(modifyProject.getTitle());
        projectDto.setType(modifyProject.getType());
        projectDto.setExport(modifyProject.getExport());
        projectDto.setSkill(modifyProject.getSkill());
        projectDto.setArea(modifyProject.getArea());
        projectDto.setContent(modifyProject.getContent());
        projectDto.setImage(modifyProject.getImage());
        projectDto.setProjectStartDate(modifyProject.getProjectStartDate());
        projectDto.setProjectEndDate(modifyProject.getProjectEndDate());
        Project project = of(projectDto);
        this.projectRepository.save(project);
        return projectDto;
    }

    //프로젝트 삭제
    public void delete(ProjectDto projectDto) {
        this.projectRepository.delete(of(projectDto));
    }

    public Specification<Project> search(String keyword) {
        return new Specification<Project>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Project> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                return criteriaBuilder.or(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.get("content"), "%" + keyword + "%"));
            }
        };
    }

    //프로젝트 좋아요
    public ProjectDto like(ProjectDto projectDto, UserDto userDto) {
        if(projectDto.getLikes().contains(userDto))
            projectDto.getLikes().remove(userDto);
        else
            projectDto.getLikes().add(userDto);
        this.projectRepository.save(of(projectDto));
        return projectDto;
    }
}
