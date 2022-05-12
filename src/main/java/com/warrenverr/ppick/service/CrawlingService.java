package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.CrawlingDto;
import com.warrenverr.ppick.model.Crawling;
import com.warrenverr.ppick.repository.CrawlingRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CrawlingService {

    private final CrawlingRepository crawlingRepository;

    private final ModelMapper modelMapper;

    private Crawling of(CrawlingDto crawlingDto) { return this.modelMapper.map(crawlingDto, Crawling.class); }


    public void createCrawling(CrawlingDto crawlingDto){
        this.crawlingRepository.save(of(crawlingDto));
    }
}
