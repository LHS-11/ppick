package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.CrawlingDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.model.CrawlingModel;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CrawlingService {

    private final CrawlingRepository crawlingRepository;

    private final ModelMapper modelMapper;

    private CrawlingModel of(CrawlingDto crawlingDto) { return this.modelMapper.map(crawlingDto, CrawlingModel.class); }


    public void createCrawling(CrawlingDto crawlingDto){
        this.crawlingRepository.save(of(crawlingDto));
    }
}
