package com.warrenverr.ppick.service;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.dto.CommentDto;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.CommentForm;
import com.warrenverr.ppick.model.Comment;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.repository.CommentRepository;
import com.warrenverr.ppick.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;

    private CommentDto of(Comment comment) { return modelMapper.map(comment, CommentDto.class); }
    private Comment of(CommentDto commentDto) { return modelMapper.map(commentDto, Comment.class); }
    private Project of(ProjectDto projectDto) { return modelMapper.map(projectDto, Project.class); }

    public CommentDto getComment(Integer id) {
        Optional<Comment> comment = this.commentRepository.findById(id);
        if(comment.isPresent())
            return of(comment.get());
        else
            throw new DataNotFoundException("comment not found");
    }

    //댓글 작성
    public void create(ProjectDto projectDto, CommentForm commentForm, UserDto userDto) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(commentForm.getContent());
        commentDto.setCreateDate(LocalDateTime.now());
        commentDto.setAuthor(userDto);
        Comment comment = this.commentRepository.save(of(commentDto));

        List<CommentDto> commentDtoList = projectDto.getCommentList();
        commentDtoList.add(of(comment));

        this.projectRepository.save(of(projectDto));
    }

    //댓글 수정
    public CommentDto modify(ProjectDto projectDto,CommentDto commentDto, CommentForm commentForm) {
        commentDto.setContent(commentForm.getContent());
        commentDto.setModifyDate(LocalDateTime.now());
        Comment comment = this.commentRepository.save(of(commentDto));

        List<CommentDto> commentDtoList = projectDto.getCommentList();
        for(int i=0;i<commentDtoList.size();i++) {
            if(Objects.equals(commentDtoList.get(i).getId(), commentDto.getId())) {
                commentDtoList.set(i,of(comment));
            }
        }
        projectDto.setCommentList(commentDtoList);
        this.projectRepository.save(of(projectDto));
        return of(comment);
    }

    //댓글 삭제
    public void delete(ProjectDto projectDto, CommentDto commentDto) {
        List<CommentDto> commentDtoList = projectDto.getCommentList();
        for(int i=0;i<commentDtoList.size();i++) {
            if(Objects.equals(commentDtoList.get(i).getId(), commentDto.getId())) {
                commentDtoList.remove(i);
            }
        }
        projectDto.setCommentList(commentDtoList);
        this.projectRepository.save(of(projectDto));

        this.commentRepository.delete(of(commentDto));

    }

}