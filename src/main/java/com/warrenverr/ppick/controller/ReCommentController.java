package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.ReCommentDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ReCommentForm;
import com.warrenverr.ppick.service.CommentService;
import com.warrenverr.ppick.service.ReCommentService;
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
@RequestMapping("/recomment")
public class ReCommentController {

    private final CommentService commentService;

    private final ReCommentService reCommentService;


    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

    //대댓글 작성
    //이때 id는 project id
    @PostMapping("/write/{id}")
    public String recommentCreate(Model model, @Valid ReCommentForm recommentForm, @PathVariable("id") Integer id,
                                HttpServletRequest request, BindingResult bindingResult) {
        UserDto userDto = getUserSession(request);
        CommentDto commentDto = commentService.getComment(id);
        if(bindingResult.hasErrors()) {
            model.addAttribute("recomment", commentDto);
            return "recomment_detail";
        }

        this.reCommentService.create(commentDto,recommentForm.getContent(), userDto);
        return String.format("redirect:/project/detail/%s",commentDto.getProject().getId());
    }

    //대댓글 수정
    @GetMapping("/modify/{id}")
    public String recommentModify(ReCommentForm recommentForm, @PathVariable("id") Integer id, HttpServletRequest request) {
        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);
        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }

        recommentForm.setContent(reCommentDto.getContent());
        return "recomment_form";
    }

    @PostMapping("/modify/{id}")
    public String recommentModify(@Valid ReCommentForm recommentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "recomment_form";
        }

        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);
        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.reCommentService.modify(reCommentDto, recommentForm.getContent());
        return String.format("redirect:/project/detail/%s", reCommentDto.getComment().getProject().getId());
    }

    //대댓글 삭제
    @GetMapping("/delete/{id}")
    public String commentDelete(@PathVariable("id") Integer id, HttpServletRequest request) {

        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);

        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
        this.reCommentService.delete(reCommentDto);
        return String.format("redirect:/project/detail/%s", reCommentDto.getComment().getProject().getId());
    }
}
