package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/project")
@SessionAttributes("userInfo")
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    //프로젝트 전체 리스트
    @RequestMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Page<ProjectDto> paging = this.projectService.getList(page, keyword);
        model.addAttribute("paging", paging);
        model.addAttribute("keyword", keyword);
        return "project_list";

    }

    //프로젝트 상세 보기
    @RequestMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, ProjectForm projectForm) {

        ProjectDto projectDto = this.projectService.getProject(id);
        model.addAttribute("project",projectDto);

        return "project_detail";

    }

    //프로젝트 작성
    @GetMapping("/write")
    public String projectCreate(ProjectForm projectForm) {
        return "project_form";
    }

    @PostMapping("/write")
    public String projectCreate(@Valid ProjectForm projectForm, BindingResult bindingResult /*TODO : 세션 정보 받아오기*/, Model model) {
        if(bindingResult.hasErrors()) {
            return "project_form";
        }

        /*TODO : 세션 정보로 유저 불러오기*/
        UserDto userDto = (UserDto) model.getAttribute("userInfo");

        this.projectService.create(projectForm,userDto);

        return "redirect:/project/list";

    }

    //프로젝트 수정
    @GetMapping("/modify/{id}")
    public String projectModify(ProjectForm projectForm, @PathVariable("id") Integer id /*TODO : 세션 정보 받아오기*/) {

        ProjectDto projectDto = this.projectService.getProject(id);

        //TODO : 수정 권한 확인
        if(true) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        projectForm.setTitle(projectDto.getTitle());
        projectForm.setType(projectDto.getType());
        projectForm.setExport(projectDto.getExport());
        projectForm.setSkill(projectDto.getSkill());
        projectForm.setArea(projectDto.getArea());
        projectForm.setContent(projectDto.getContent());
        projectForm.setImage(projectDto.getImage());
        projectForm.setProjectDate(projectDto.getProjectDate());
        return "project_form";

    }

    @PostMapping("/modify/{id}")
    public String projectModify(@Valid ProjectForm projectForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id /*TODO : 세션 정보 받아오기*/) {

        if(bindingResult.hasErrors()) {
            return "project_form";
        }

        ProjectDto projectDto = this.projectService.getProject(id);

        //TODO : 수정 권한 확인
        if(true) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.projectService.modify(projectDto, projectForm);
        return String.format("redirect:/question/detail/%s",id);

    }

    //프로젝트 삭제
    @GetMapping("/delete/{id}")
    public String projectDelete(@PathVariable("id") Integer id /*TODO : 세션 정보 받아오기*/) {

        ProjectDto projectDto = this.projectService.getProject(id);

        //TODO : 삭제 권한 확인
        if(true) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.projectService.delete(projectDto);
        return "redirect:/";
    }


    //프로젝트 추천 누르기
    @GetMapping("/like/{id}")
    public String projectLike(@PathVariable("id") Integer id /*TODO : 세션 정보 받아오기*/) {

        ProjectDto projectDto = this.projectService.getProject(id);
        /*TODO : 세션 정보로 유저 불러오기*/
        UserDto userDto = new UserDto();
        this.projectService.like(projectDto,userDto);
        return String.format("redirect:/question/detail/%s", id);

    }


}
