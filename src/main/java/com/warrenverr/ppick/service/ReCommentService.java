package com.warrenverr.ppick.service;

        import com.warrenverr.ppick.DataNotFoundException;
        import com.warrenverr.ppick.dto.CommentDto;
        import com.warrenverr.ppick.dto.ReCommentDto;
        import com.warrenverr.ppick.dto.UserDto;
        import com.warrenverr.ppick.model.ReComment;
        import com.warrenverr.ppick.repository.ReCommentRepository;
        import lombok.RequiredArgsConstructor;
        import org.modelmapper.ModelMapper;
        import org.springframework.stereotype.Service;

        import java.time.LocalDateTime;
        import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReCommentService {

    private final ReCommentRepository reCommentRepository;
    private final ModelMapper modelMapper;

    public ReCommentDto of(ReComment reComment){
        return modelMapper.map(reComment, ReCommentDto.class);
    }

    public ReComment of(ReCommentDto reCommentDto){
        return modelMapper.map(reCommentDto, ReComment.class);
    }

    //대댓글 생성
    public ReCommentDto create(CommentDto commentDto, String content, UserDto author){
        ReCommentDto reCommentDto = new ReCommentDto();
        reCommentDto.setAuthor(author);
        reCommentDto.setComment(commentDto);
        reCommentDto.setContent(content);
        reCommentDto.setCreateDate(LocalDateTime.now());
        ReComment reComment = of(reCommentDto);
        reCommentRepository.save(reComment);
        return reCommentDto;
    }

    public ReCommentDto getReComment(Integer id){
        Optional<ReComment> reComment = reCommentRepository.findById(id);
        if(reComment.isPresent()){
            return of(reComment.get());
        }else{
            throw new DataNotFoundException("recomment not found");
        }
    }

    //대댓글 수정
    public ReCommentDto modify(ReCommentDto reCommentDto,String content){
        reCommentDto.setContent(content);
        reCommentDto.setModifyDate(LocalDateTime.now());
        reCommentRepository.save(of(reCommentDto));
        return reCommentDto;
    }

    //대댓글 삭제
    public void delete(ReCommentDto reCommentDto){
        reCommentRepository.delete(of(reCommentDto));
    }
}