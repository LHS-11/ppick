package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Contest;
import com.warrenverr.ppick.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest,Integer> {

    Page<Contest> findAll(Specification<Contest> specification, Pageable pageable);

}
