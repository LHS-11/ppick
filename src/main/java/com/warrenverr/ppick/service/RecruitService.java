package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.RecruitDto;
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

    private Recruit of(RecruitDto recruitDto) { return modelMapper.map(recruitDto, Recruit.class); }
        //모집 인원 저장
    public Recruit create(RecruitDto recruitDto) {
        return this.recruitRepository.save(of(recruitDto));
    }

    public Recruit modify(RecruitDto modifyRecruitDto) {
        return this.recruitRepository.save(of(modifyRecruitDto));
    }

}
