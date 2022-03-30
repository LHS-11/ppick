package com.warrenverr.ppick.service;

import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    //프로젝트 생성
    public Project create(ProjectForm projectForm, User user) {
        Project project = new Project();
        project.setTitle(projectForm.getTitle());
        project.setType(projectForm.getType());
        project.setExport(projectForm.getExport());
        project.setSkill(projectForm.getSkill());
        project.setArea(projectForm.getArea());
        project.setContent(projectForm.getContent());
        project.setImage(projectForm.getImage());
        project = this.projectRepository.save(project);
        return project;
    }

    public Optional<Project> getProject(Integer id) {
        return this.projectRepository.findById(id);
    }

    public Page<Project> getList(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page,10, Sort.by(sorts));
        Specification<Project> specification = search(keyword);
        Page<Project> projectList = this.projectRepository.findAll(specification, pageable);
        return projectList;
    }

    //프로젝트 수정
    public Project modify(Project project, ProjectForm modifyProject) {
        project.setTitle(modifyProject.getTitle());
        project.setType(modifyProject.getType());
        project.setExport(modifyProject.getExport());
        project.setSkill(modifyProject.getSkill());
        project.setArea(modifyProject.getArea());
        project.setContent(modifyProject.getContent());
        project.setImage(modifyProject.getImage());
        project = this.projectRepository.save(project);
        return project;
    }

    //프로젝트 삭제
    public void delete(Project project) {
        this.projectRepository.delete(project);
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
}
