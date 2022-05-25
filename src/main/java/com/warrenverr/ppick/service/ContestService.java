package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.ContestDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.model.Contest;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.repository.ContestRepository;
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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContestService {

    private final ContestRepository contestRepository;

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    private Contest of(ContestDto contestDto) { return this.modelMapper.map(contestDto, Contest.class); }

    private ProjectDto of(Project project) {
        return this.modelMapper.map(project, ProjectDto.class);
    }
    private ContestDto of(Contest contest) {
        return this.modelMapper.map(contest, ContestDto.class);}

    public void createCrawling(ContestDto contestDto){
        this.contestRepository.save(of(contestDto));
    }

    //공모전 전체 리스트 및 키워드 서치
//    @Transactional
    public Page<ContestDto> getListPaging(int page, String keyword) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page,8, Sort.by(sorts));
        Specification<Contest> specification = searchPaging(keyword);
        Page<Contest> contestList = this.contestRepository.findAll(specification, pageable);
        Page<ContestDto> contestDtoList = contestList.map(contest -> of(contest));
        return contestDtoList;
    }

    @Transactional
    public List<ContestDto> getList(String keyword){
        List<Contest> contestList = new ArrayList<>();
        if(keyword==""){
            contestList = contestRepository.findAll();
        }else{
            contestList = contestRepository.findByTitleContains(keyword);
        }
        List<ContestDto> contestDtoList = contestList.stream().map(contest -> of(contest)).collect(Collectors.toList());
        return contestDtoList;
    }

    //공모전 서치
    public Specification<Contest> searchPaging(String keyword) {
        return new Specification<Contest>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Contest> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                query.distinct(true);
                return criteriaBuilder.like(root.get("title"), "%" + keyword + "%");

            }
        };
    }

    public List<Contest> search(String keyword){
        List<Contest> contestList = this.contestRepository.findByTitleContains(keyword);
        return contestList;
    }

    //공모전 상세 페이지
    @Transactional
    public ContestDto getContest(Integer id) {
        return of(contestRepository.findById(id).get());
    }

    //공모전 관련 프로젝트 지원하기


    //공모전 관련 프로젝트 보여주기
    @Transactional
    public Page<ProjectDto> getProjectList(int page, Integer id) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.asc("createDate"));
        Pageable pageable = PageRequest.of(page,8, Sort.by(sorts));
        Page<Project> projectList = this.projectRepository.findByContest(id, pageable);
        projectList.map(project -> of(project));
        Page<ProjectDto> projectDtoList = projectList.map(project -> of(project));
        return projectDtoList;
    }


}
