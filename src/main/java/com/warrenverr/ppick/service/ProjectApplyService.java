package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ProjectApplyDto;
import com.warrenverr.ppick.model.ProjectApply;
import com.warrenverr.ppick.repository.ProjectApplyRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectApplyService {

    private final ProjectApplyRepository projectApplyRepository;

    private final ModelMapper modelMapper;

    private ProjectApply of(ProjectApplyDto projectApplyDto) { return modelMapper.map(projectApplyDto, ProjectApply.class); }


    public ProjectApply create(ProjectApplyDto projectApplyDto) {
        return this.projectApplyRepository.save(of(projectApplyDto));
    }


}
