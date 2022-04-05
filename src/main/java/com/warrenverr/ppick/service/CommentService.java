package com.warrenverr.ppick.service;

import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.CommentForm;
import com.warrenverr.ppick.model.Comment;
import com.warrenverr.ppick.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    private CommentDto of(Comment comment) { return modelMapper.map(comment, CommentDto.class); }
    private Comment of(CommentDto commentDto) { return modelMapper.map(commentDto, Comment.class); }


}
