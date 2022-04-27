package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.Recruit;
import com.warrenverr.ppick.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private final ModelMapper modelMapper;

    private ProjectDto of(Project project) { return modelMapper.map(project, ProjectDto.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }
    //모집 인원 저장
    public void create(Project project, List<Recruit> recruits) {
        for (int i=0; i<recruits.size();i++)
            recruits.get(i).setProject(project);
        this.recruitRepository.saveAll(recruits);
    }

    public void modify(Project project, List<Recruit> recruits,List<Recruit> modifyRecruits) {
        for (int i=0; i<modifyRecruits.size();i++)
            modifyRecruits.get(i).setProject(project);
        this.recruitRepository.deleteAll(recruits);
        this.recruitRepository.saveAll(modifyRecruits);
    }
}
