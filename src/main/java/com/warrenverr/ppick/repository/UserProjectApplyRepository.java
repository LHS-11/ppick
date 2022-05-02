package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.model.UserProjectApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProjectApplyRepository extends JpaRepository<UserProjectApply,Integer> {

    Optional<UserProjectApply> findByUserAndProject(User user, Project project);
}
