package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectApplyForm;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
/*import io.jsonwebtoken.ExpiredJwtException;*/
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final UserService userService;
    private final ProjectService projectService;
    private final ModelMapper modelMapper;

    /*@Autowired
    private JwtTokenUtil jwtTokenUtil;*/

    private ProjectForm of(ProjectDto projectDto) {
        return modelMapper.map(projectDto, ProjectForm.class);
    }

    private ProjectDto of(Project project) {
        return modelMapper.map(project, ProjectDto.class);
    }

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

    //Decode Token
    /*public String decodeJWT(HttpServletRequest request) {
        String snsid;
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        try {
            snsid = jwtTokenUtil.getSnsidFromToken(jwtToken);
            return snsid;
        }  catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }
        return null;
    }*/


    //프로젝트 전체 리스트
    @GetMapping("/list")
    public ResponseEntity<?> listByPageAndKeyword(@RequestParam(value = "page", defaultValue = "0") int page,
                                  @RequestParam(value = "keyword", defaultValue = "") String keyword) {

        Page<ProjectDto> paging = this.projectService.getListByPageAndKeyword(page, keyword);
        return new ResponseEntity<>(paging.getContent(), HttpStatus.OK);
    }

    @GetMapping("/listBySkill")
    public ResponseEntity<?> listBySkill(@RequestParam(value = "skill") String skill) {
        List<ProjectDto>  projectDtoList = this.projectService.getListBySkill(skill);
        return new ResponseEntity<>(projectDtoList, HttpStatus.OK);
    }



    //프로젝트 상세 보기
    @GetMapping(value = "/detail/{id}")
    public ResponseEntity<?> detail(@PathVariable("id") Integer id) {
        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }


    @PostMapping("/write")
    public ResponseEntity<?> projectCreate(@Valid @RequestBody ProjectForm projectForm, HttpServletRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }


        /*String snsid = "ㄴㅅ"*//*decodeJWT(request)*//*;
        UserDto userDto = this.userService.loginBySnsid(snsid);*/
        UserDto userDto = null;
//        if (userDto == null) {
//            return new ResponseEntity<>("FAIL", HttpStatus.UNAUTHORIZED);
//        }
        this.projectService.create(projectForm, userDto);

        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //프로젝트 수정
    @GetMapping("/modify/{id}")
    public ResponseEntity<?> projectModify(@PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        UserDto userDto = getUserSession(request);
        ProjectForm projectForm;
        projectForm = of(projectDto);
        projectForm.setMainTask(projectDto.getRecruit().getMainTask());
        projectForm.setSubTask(projectDto.getRecruit().getSubTask());
        projectForm.setRecruitment(projectDto.getRecruit().getRecruitment());

        return new ResponseEntity<>(projectForm, HttpStatus.OK);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> projectModify(@Valid @RequestBody ProjectForm projectForm, BindingResult bindingResult,
                                                @PathVariable("id") Integer id, HttpServletRequest request) {

        UserDto userDto = getUserSession(request);
        ProjectDto projectDto = this.projectService.getProjectByPid(id);

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);

        }

//        if(!projectDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            return new ResponseEntity<>("FAIL",HttpStatus.FORBIDDEN);
//        }

        this.projectService.modify(projectDto, projectForm);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);

    }

    //프로젝트 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> projectDelete(@PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        UserDto userDto = getUserSession(request);

//        if(!projectDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            return new ResponseEntity<>("FAIL",HttpStatus.FORBIDDEN);
//        }

        this.projectService.delete(projectDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }


    //프로젝트 추천 누르기
    @GetMapping("/like/{id}")
    public ResponseEntity<?> projectLike(@PathVariable("id") Integer id, HttpServletRequest request) {

        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        UserDto userDto = getUserSession(request);

//        if (userDto == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "좋아요 권한이 없습니다.");
//        }

        projectDto = this.projectService.like(projectDto, userDto);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);
    }

    //프로젝트 신청
    @PostMapping("/ppick/{id}")
    public ResponseEntity<?> projectApply(@PathVariable("id") Integer id, @Valid @RequestBody ProjectApplyForm projectApplyForm, BindingResult bindingResult, HttpServletRequest request) {
        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        UserDto userDto = getUserSession(request);
        /*if(bindingResult.hasErrors()) {
            return "project_form";
        }
        List<ProjectApplyDto> projectApplyDtoList =  projectDto.getApplyList();
        for(int i=0;i<projectApplyDtoList.size();i++) {
            if(projectApplyDtoList.get(i).getUserDto().getEmail().equals(userDto.getEmail())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 신청");
            }
        }*/
        projectService.apply(projectDto, userDto, projectApplyForm);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

}
