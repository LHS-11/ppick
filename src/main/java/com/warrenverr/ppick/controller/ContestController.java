package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.ContestDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.model.Contest;
import com.warrenverr.ppick.service.ContestService;
import com.warrenverr.ppick.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping("/contest")
public class ContestController {


    private final ContestService contestService;

    private final ProjectService projectService;

    private final ModelMapper modelMapper;

    private ContestDto of(Contest contest){
        return modelMapper.map(contest, ContestDto.class);
    }

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

    //공모전 전체 리스트,공모전 찾기 버튼 클릭 컨트롤러
    @GetMapping("/list")
    public ResponseEntity<?> list(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Page<ContestDto> paging = this.contestService.getList(page, keyword);
        return new ResponseEntity<>(paging.getContent(), HttpStatus.OK);
    }


    //공모전 상세 페이지 컨트롤러
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        ContestDto contestDto = this.contestService.getContest(id);
        return new ResponseEntity<>(contestDto, HttpStatus.OK);
    }

    //공모전 관련 프로젝트 컨트롤러 -> 페이징 처리 여부
    @GetMapping(value = "/category")
    public ResponseEntity<?> category(@RequestParam(value = "page",defaultValue = "0") int page,
                                      @RequestParam(value="id") Integer id){
        Page<ProjectDto> paging = this.contestService.getProjectList(page, id);
        return new ResponseEntity<>(paging.getContent(), HttpStatus.OK);
    }
}
