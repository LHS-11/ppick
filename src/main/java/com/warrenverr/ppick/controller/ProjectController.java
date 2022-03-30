package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    private final UserService userService;

    @GetMapping("/write")
    public String projectCreate(ProjectForm projectForm) {
        return "project_form";
    }

//    @PostMapping("/write")
//    public String projectCreate(@Valid ProjectForm projectForm, BindingResult bindingResult) {
//
//        if(bindingResult.hasErrors()){
//            return "project_form";
//        }
//    }

}
