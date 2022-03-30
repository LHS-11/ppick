package com.warrenverr.ppick;

import static org.junit.jupiter.api.Assertions.*;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.repository.ProjectRepository;
import com.warrenverr.ppick.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class PpickApplicationTests {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	@Test
	void create_test() {
		Project project = new Project();
		project.setTitle("제목2");
		project.setType("프로젝트 모집");
		project.setExport("웹");
		project.setSkill("백엔드");
		project.setArea("경기도");
		project.setContent("내용");
		project.setImage("image");
		project.setProjectDate("오늘");
		this.projectRepository.save(project);
	}

	@Test
	void findAll_test() {
		List<Project> projectList = this.projectRepository.findAll();
		assertEquals(2,projectList.size());
	}

	@Test
	void search_test() {
		Page<Project> projectList = this.projectService.getList(0,"제목2");

		for (Project p : projectList) {
			System.out.println(p.getTitle());
		}
	}

	@Test
	void delete_test() {
		Optional<Project> project = this.projectRepository.findById(1);
		Project p = project.get();
		this.projectService.delete(p);
		List<Project> projectList = this.projectRepository.findAll();
		assertEquals(1,projectList.size());
	}


}
