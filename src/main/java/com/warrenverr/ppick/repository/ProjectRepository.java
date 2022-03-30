package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Page<Project> findAll(Specification<Project> specification, Pageable pageable);
}
