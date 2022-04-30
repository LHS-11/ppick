package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.RecruitDto;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.Recruit;
import com.warrenverr.ppick.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RecruitService {

    private final RecruitRepository recruitRepository;

    private final ModelMapper modelMapper;

    private List<Recruit> of(List<RecruitDto> recruitDtoList) { return modelMapper.map(recruitDtoList, new TypeToken<List<Recruit>>() {}.getType()); }
    //모집 인원 저장
    public void create(Project project, List<RecruitDto> recruitDtoList) {
        for (int i=0; i<recruitDtoList.size();i++) {
            recruitDtoList.get(i).setProject(project);
        }
        this.recruitRepository.saveAll(of(recruitDtoList));
    }

    public void modify(Project project, List<RecruitDto> recruitDtoList, List<RecruitDto> modifyRecruitDtoList) {
        for (int i=0; i<modifyRecruitDtoList.size();i++)
            modifyRecruitDtoList.get(i).setProject(project);
        this.recruitRepository.deleteAll(of(recruitDtoList));
        this.recruitRepository.saveAll(of(modifyRecruitDtoList));
    }
}
