package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject,Integer> {
}
