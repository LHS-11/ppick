package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.CommentForm;
import com.warrenverr.ppick.service.CommentService;
import com.warrenverr.ppick.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final ProjectService projectService;
    private final CommentService commentService;

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }
    //댓글 작성
    //이때 id는 project id
    @PostMapping("/write/{id}")
    public String commentCreate(Model model, @Valid CommentForm commentForm, @PathVariable("id") Integer id,
                                HttpServletRequest request, BindingResult bindingResult) {
        UserDto userDto = getUserSession(request);
        ProjectDto projectDto = this.projectService.getProject(id);
        if(bindingResult.hasErrors()) {
            model.addAttribute("project", projectDto);
            return "project_detail";
        }

        this.commentService.create(projectDto, commentForm, userDto);
        return String.format("redirect:/project/detail/%s",id);
    }
    //댓글 수정
    @GetMapping("/modify/{id}")
    public String commentModify(CommentForm commentForm, @PathVariable("id") Integer id, HttpServletRequest request) {
        CommentDto commentDto = this.commentService.getComment(id);
        UserDto userDto = getUserSession(request);
        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        commentForm.setContent(commentDto.getContent());
        return "comment_form";
    }

    @PostMapping("/modify/{id}")
    public String commentModify(@Valid CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "comment_form";
        }

        CommentDto commentDto = this.commentService.getComment(id);
        UserDto userDto = getUserSession(request);
        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.commentService.modify(commentDto, commentForm);
        return String.format("redirect:/project/detail/%s",commentDto.getId());
    }

    //댓글 삭제
    @GetMapping("/delete/{id}")
    public String commentDelete(@PathVariable("id") Integer id, HttpServletRequest request) {
        CommentDto commentDto = this.commentService.getComment(id);
        UserDto userDto = getUserSession(request);


        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.commentService.delete(commentDto);
        return String.format("redirect:/project/detail/%s",commentDto.getId());
    }
}