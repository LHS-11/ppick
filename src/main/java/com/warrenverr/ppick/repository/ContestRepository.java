package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest,Integer> {

    Page<Contest> findAll(Specification<Contest> specification, Pageable pageable);

    List<Contest> findByTitleContains(String title);
}
