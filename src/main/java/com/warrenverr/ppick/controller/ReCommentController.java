package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.ReCommentDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ReCommentForm;
import com.warrenverr.ppick.service.CommentService;
import com.warrenverr.ppick.service.ReCommentService;
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
@RequestMapping("/api/recomment")
public class ReCommentController {

    private final CommentService commentService;

    private final ReCommentService reCommentService;
    private final UserService userService;

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

    //대댓글 작성
    //이때 id는 project id
    @PostMapping("/write/{id}")
    public ResponseEntity<?> recommentCreate(Model model, @Valid @RequestBody ReCommentForm recommentForm, @PathVariable("id") Integer id,
                                HttpServletRequest request, BindingResult bindingResult) {
        UserDto userDto = userService.findUserById(1L);
        CommentDto commentDto = commentService.getComment(id);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }

        this.reCommentService.create(commentDto,recommentForm.getContent(), userDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //대댓글 수정
    @GetMapping("/modify/{id}")
    public ResponseEntity<?> recommentModify(@PathVariable("id") Integer id, HttpServletRequest request) {
        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);
//        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
        ReCommentForm reCommentForm = new ReCommentForm();
        reCommentForm.setContent(reCommentDto.getContent());
        return new ResponseEntity<>(reCommentForm, HttpStatus.OK);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<?> recommentModify(@Valid @RequestBody ReCommentForm recommentForm, BindingResult bindingResult,
                                @PathVariable("id") Integer id, @RequestParam(value = "commentId") Integer commentId, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
        }
        CommentDto commentDto = commentService.getComment(commentId);
        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);
//        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
        this.reCommentService.modify(commentDto,reCommentDto, recommentForm.getContent());
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    //대댓글 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> commentDelete(@PathVariable("id") Integer id, @RequestParam(value = "commentId") Integer commentId, HttpServletRequest request) {

        CommentDto commentDto = commentService.getComment(commentId);
        ReCommentDto reCommentDto = reCommentService.getReComment(id);
        UserDto userDto = getUserSession(request);

//        if(!reCommentDto.getAuthor().getEmail().equals(userDto.getEmail())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
//        }
        this.reCommentService.delete(commentDto, reCommentDto);
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
