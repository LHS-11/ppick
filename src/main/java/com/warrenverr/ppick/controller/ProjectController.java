package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.model.Recruit;
import com.warrenverr.ppick.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/project")
@ResponseBody
public class ProjectController {

    private final ProjectService projectService;


    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

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
    public ProjectDto detail(Model model, @PathVariable("id") Integer id) {

        ProjectDto projectDto = this.projectService.getProject(id);
        model.addAttribute("project",projectDto);

        return projectDto;

    }

    //프로젝트 작성
    @GetMapping("/write")
    public String projectCreate(ProjectForm projectForm) {
        return "project_form";
    }

    @PostMapping("/write")
    public String projectCreate(ProjectForm projectForm, HttpServletRequest request, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "project_form";
        }
        UserDto userDto = getUserSession(request);
        this.projectService.create(projectForm, userDto);

        return "redirect:/project/list";

    }

    //프로젝트 수정
    @GetMapping("/modify/{id}")
    public String projectModify(ProjectForm projectForm, @PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProject(id);
        UserDto userDto = getUserSession(request);

        if(!projectDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }


        projectForm.setTitle(projectDto.getTitle());
        projectForm.setType(projectDto.getType());
        projectForm.setExport(projectDto.getExport());
        projectForm.setSkill(projectDto.getSkill());
        projectForm.setArea(projectDto.getArea());
        projectForm.setContent(projectDto.getContent());
        projectForm.setImage(projectDto.getImage());
        projectForm.setProjectStartDate(projectDto.getProjectStartDate());
        projectForm.setProjectEndDate(projectDto.getProjectEndDate());

        //일단 0번방 꺼만 수정시 화면에 보이게 해놨어요
        // Recruit의 0번방이 첫번째 데이터들이에요 이거 main 0, 1, 2 합친 상태로 mainTask에 뿌려줘야해요 이건 제가 나중에 할게요.
        projectForm.setMainTask(projectDto.getRecruitList().get(0).getMainTask());
        projectForm.setSubTask(projectDto.getRecruitList().get(0).getSubTask());
        projectForm.setRecruitment(projectDto.getRecruitList().get(0).getRecruitment());
        return "project_form";

    }

    @PostMapping("/modify/{id}")
    public String projectModify(@Valid @RequestBody ProjectForm projectForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, HttpServletRequest request) {

        UserDto userDto = getUserSession(request);
        ProjectDto projectDto = this.projectService.getProject(id);

        if(bindingResult.hasErrors()) {
            return "project_form";
        }

        if(!projectDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.projectService.modify(projectDto, projectForm);
        return String.format("redirect:/project/detail/%s",id);

    }

    //프로젝트 삭제
    @GetMapping("/delete/{id}")
    public String projectDelete(@PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProject(id);
        UserDto userDto = getUserSession(request);

        if(!projectDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.projectService.delete(projectDto);
        return "redirect:/";
    }


    //프로젝트 추천 누르기
    @GetMapping("/like/{id}")
    public String projectLike(@PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProject(id);
        UserDto userDto = getUserSession(request);

        this.projectService.like(projectDto, userDto);
        return String.format("redirect:/project/detail/%s", id);

    }

    //프로젝트 신청
    @GetMapping("/ppick/{id}")
    public String projectApply(@PathVariable("id") Integer id) {

        return "project_apply";
    }

    @PostMapping("/ppick/{id}")
    public String projectApply(@PathVariable("id") Integer id, HttpServletRequest request) {
        ProjectDto projectDto = this.projectService.getProject(id);
        UserDto userDto = getUserSession(request);

        this.projectService.apply(projectDto,userDto);

        return String.format("redirect:/project/detail/%s", id);

    }
}
