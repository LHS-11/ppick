package com.warrenverr.ppick.repository;

import com.warrenverr.ppick.model.CrawlingModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrawlingRepository extends JpaRepository<CrawlingModel,Integer> {
}
