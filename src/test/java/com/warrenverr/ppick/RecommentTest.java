package com.warrenverr.ppick;

import com.warrenverr.ppick.dto.ReCommentDto;
import com.warrenverr.ppick.model.ReComment;
import com.warrenverr.ppick.repository.ReCommentRepository;
import com.warrenverr.ppick.service.ReCommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class RecommentTest {

    @Autowired
    private ReCommentService reCommentService;

    @Autowired
    private ReCommentRepository reCommentRepository;

    @Test
    void 리코멘트_생성(){

        reCommentService.create(null,"test용",null);
    }

    @Test
    void 리코멘트_수정(){
        Optional<ReComment> re = reCommentRepository.findById(2);
        ReComment reComment = re.get();
        reCommentService.modify(reCommentService.of(reComment), "수정됐나요?");
    }

    @Test
    void 리코멘트_삭제(){
        Optional<ReComment> reComment = reCommentRepository.findById(1);
        ReComment reComment1 = reComment.get();
        reCommentService.delete(reCommentService.of(reComment1));
    }
}
