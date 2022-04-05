package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
