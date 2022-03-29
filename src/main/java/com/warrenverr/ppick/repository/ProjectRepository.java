package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

}
