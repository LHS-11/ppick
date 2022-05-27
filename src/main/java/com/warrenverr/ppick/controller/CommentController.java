package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.CommentForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.service.CommentService;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final ProjectService projectService;
    private final CommentService commentService;
    private final UserService userService;

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }
    //댓글 작성
    //이때 id는 project id
    @PostMapping("/write/{id}")
    public ResponseEntity<?> commentCreate(@PathVariable("id") Integer id, @Valid @RequestBody CommentForm commentForm,
                                        HttpServletRequest request, BindingResult bindingResult) {
        UserDto userDto = this.userService.findUserById(1L);
        ProjectDto projectDto = this.projectService.getProjectByPid(id);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        this.commentService.create(projectDto, commentForm, userDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
    //댓글 수정
    @GetMapping("/modify/{id}")
    public ResponseEntity<?>  commentModify(@PathVariable("id") Integer id, HttpServletRequest request) {
        CommentDto commentDto = this.commentService.getComment(id);
        UserDto userDto = getUserSession(request);
//        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
        CommentForm commentForm = new CommentForm();
        commentForm.setContent(commentDto.getContent());
        return new ResponseEntity<>(commentForm, HttpStatus.OK);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> commentModify(@Valid @RequestBody CommentForm commentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, @RequestParam(value = "projectId") Integer projectId, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        CommentDto commentDto = this.commentService.getComment(id);
        ProjectDto projectDto = this.projectService.getProjectByPid(projectId);
        UserDto userDto = getUserSession(request);
//        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
        this.commentService.modify(projectDto,commentDto, commentForm);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable("id") Integer id, @RequestParam(value = "projectId") Integer projectId , HttpServletRequest request) {
        ProjectDto projectDto = this.projectService.getProjectByPid(projectId);
        CommentDto commentDto = this.commentService.getComment(id);
        UserDto userDto = getUserSession(request);


//        if(!commentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
//        }
        this.commentService.delete(projectDto, commentDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}