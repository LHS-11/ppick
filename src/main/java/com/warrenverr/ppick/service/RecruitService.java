package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.RecruitDto;
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
    public void create(List<RecruitDto> recruitDtoList) {
        this.recruitRepository.saveAll(of(recruitDtoList));
    }

    public void modify(List<RecruitDto> recruitDtoList, List<RecruitDto> modifyRecruitDtoList) {
        this.recruitRepository.deleteAll(of(recruitDtoList));
        this.recruitRepository.saveAll(of(modifyRecruitDtoList));
    }
}
