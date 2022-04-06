/*
package com.warrenverr.ppick;

import static org.junit.jupiter.api.Assertions.*;

import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.ProjectForm;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.Project;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.repository.ProjectRepository;
import com.warrenverr.ppick.repository.UserRepository;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@WebAppConfiguration
@SpringBootTest
class PpickApplicationTests {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserService userService;


	@Test
	void signup_test() {
		UserCreateForm userCreateForm = new UserCreateForm();
		userCreateForm.setSns_id("444");
		userCreateForm.setEmail("444@daum.net");
		userCreateForm.setNickname("444");
		userCreateForm.setSkill("JAVAScript");
		userCreateForm.setJob("Student");
		userCreateForm.setCategory("백엔드");
		userCreateForm.setDetail_category("JAVA");
		userCreateForm.setAgree("Y");

		UserDto userDto = userService.signup(userCreateForm);
	}

	@Test
	void login_test() {
		UserDto userDto = userService.loginByEmail("444@daum.net");
	}

	@Test
	void selectUser_test() {
		List<User> userList = this.userRepository.findAll();
		assertEquals("444", userList.get(1).getSns_id());
	}


	@Test
	void create_test() {
		ProjectForm projectForm = new ProjectForm();
		projectForm.setTitle("타이틀2");
		projectForm.setType("프로젝트 모집");
		projectForm.setExport("웹");
		projectForm.setSkill("백엔드");
		projectForm.setArea("서울");
		projectForm.setContent("내용");
		projectForm.setImage("image");
		projectForm.setProjectDate("내일");

		*/
/*ProjectDto projectDto = projectService.create(projectForm);*//*

	}

	@Test
	void get_test() {
		ProjectDto projectDto = this.projectService.getProject(1);
		System.out.println(projectDto.getArea());
	}

	@Test
	void findAll_test() {
		List<Project> projectList = this.projectRepository.findAll();
		assertEquals(2,projectList.size());
	}

	@Test
	void search_test() {
		Page<ProjectDto> projectDtoList = this.projectService.getList(0,"타이");

		for (ProjectDto p : projectDtoList) {
			System.out.println(p.getTitle());
		}
	}

	@Test
	void modify_test() {
		ProjectDto projectDto = this.projectService.getProject(2);
		ProjectForm projectForm = new ProjectForm();
		projectForm.setTitle("제목1");
		projectForm.setType("프로젝트 모집");
		projectForm.setExport("앱");
		projectForm.setSkill("프론트엔드");
		projectForm.setArea("강원");
		projectForm.setContent("내용없음");
		projectForm.setImage("image123");
		projectForm.setProjectDate("그저께");
		this.projectService.modify(projectDto,projectForm);
	}

	@Test
	void delete_test() {
		ProjectDto projectDto = this.projectService.getProject(1);
		this.projectService.delete(projectDto);
		List<Project> projectList = this.projectRepository.findAll();
		assertEquals(1,projectList.size());
	}


}
*/
