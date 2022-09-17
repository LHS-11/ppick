package com.warrenverr.ppick.service;

import com.warrenverr.ppick.config.DataNotFoundException;
import com.warrenverr.ppick.dto.ProjectApplyDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.RecruitDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectApplyForm;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.ProjectApply;
import com.warrenverr.ppick.model.Recruit;
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

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final RecruitService recruitService;

    private final ProjectApplyService projectApplyService;

    private  final ModelMapper modelMapper;

    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }
    private ProjectDto of(ProjectForm projectForm) { return modelMapper.map(projectForm, ProjectDto.class); }
    private RecruitDto of(Recruit recruit) { return modelMapper.map(recruit, RecruitDto.class); }
    private ProjectApplyDto of(ProjectApply projectApply) { return modelMapper.map(projectApply, ProjectApplyDto.class); }
    //프로젝트 생성
    @Transactional
    public ProjectDto create(ProjectForm projectForm, UserDto userDto) {
        ProjectDto projectDto = new ProjectDto();
        RecruitDto recruitDto = new RecruitDto();
        recruitDto.setMainTask(projectForm.getMainTask());
        recruitDto.setSubTask(projectForm.getSubTask());
        recruitDto.setRecruitment(projectForm.getRecruitment());
        Recruit recruit = recruitService.create(recruitDto);

        projectDto = of(projectForm);
        projectDto.setAuthor(userDto);
        projectDto.setRecruit(of(recruit));
        Project project = this.projectRepository.save(of(projectDto));
        return of(project);
    }

    //프로젝트 조회
    public ProjectDto getProjectByPid(Integer id) {
        Optional<Project> project = this.projectRepository.findById(id);
        if(project.isPresent()) {
            return of(project.get());
        } else {
            throw new DataNotFoundException("project not found");
        }
    }

    public Page<ProjectDto> getListByPageAndKeyword(int limit, String keyword) {
        if(limit==0)
            limit=10;
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createDate"));
        Pageable pageable = PageRequest.of(0,limit, Sort.by(sorts));
        Specification<Project> specification = search(keyword);
        Page<Project> projectList = this.projectRepository.findAll(specification, pageable);
        Page<ProjectDto> projectDtoList = projectList.map(project -> of(project));
        return projectDtoList;
    }

    public List<ProjectDto> getListByKeyword(String keyword) {
        Specification<Project> specification = search(keyword);
        List<Project> projectList = this.projectRepository.findAll(specification);
        return projectList.stream().map(p -> modelMapper.map(p, ProjectDto.class)).collect(Collectors.toList());
    }

    public List<ProjectDto> getListBySkill(String skill) {
        List<Project> projectList = projectRepository.findAllBySkill(skill);
        return projectList.stream().map(p -> modelMapper.map(p, ProjectDto.class)).collect(Collectors.toList());
    }



    //프로젝트 수정
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
    public ProjectDto modify(ProjectDto projectDto, ProjectForm modifyProject) {
        RecruitDto recruitDto = projectDto.getRecruit();
        recruitDto.setMainTask(modifyProject.getMainTask());
        recruitDto.setSubTask(modifyProject.getSubTask());
        recruitDto.setRecruitment(modifyProject.getRecruitment());
        Recruit recruit = recruitService.modify(recruitDto);


        projectDto.setTitle(modifyProject.getTitle());
        projectDto.setType(modifyProject.getType());
        projectDto.setExport(modifyProject.getExport());
        projectDto.setSkill(modifyProject.getSkill());
        projectDto.setArea(modifyProject.getArea());
        projectDto.setContent(modifyProject.getContent());
        projectDto.setImage(modifyProject.getImage());
        projectDto.setProjectStartDate(modifyProject.getProjectStartDate());
        projectDto.setProjectEndDate(modifyProject.getProjectEndDate());
        projectDto.setRecruit(of(recruit));
        Project project = of(projectDto);
        this.projectRepository.save(project);

        return projectDto;
    }

    //프로젝트 삭제
    public void delete(ProjectDto projectDto) {
        this.projectRepository.delete(of(projectDto));
    }


    //프로젝트 좋아요
    public ProjectDto like(ProjectDto projectDto, UserDto userDto) {
        if(projectDto.getLikes().contains(userDto.getId()))
            projectDto.getLikes().remove(userDto.getId());
        else
            projectDto.getLikes().add(userDto.getId());
        Project project = this.projectRepository.save(of(projectDto));
        return of(project);
    }

    //프로젝트 지원
    public void apply(ProjectDto projectDto, UserDto userDto, ProjectApplyForm projectApplyForm) {
        ProjectApplyDto projectApplyDto = new ProjectApplyDto();
        projectApplyDto.setField(projectApplyForm.getField());
        projectApplyDto.setMotive(projectApplyForm.getMotive());
        projectApplyDto.setUserDto(userDto);

        ProjectApply projectApply = this.projectApplyService.create(projectApplyDto);

        List<ProjectApplyDto> projectApplyDtoList = projectDto.getApplyList();
        projectApplyDtoList.add(of(projectApply));
        projectDto.setApplyList(projectApplyDtoList);
        this.projectRepository.save(of(projectDto));

    }
}
