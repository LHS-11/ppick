package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
@SessionAttributes("userInfo")
public class UserController {

    @ModelAttribute("userInfo")
    public User setUpUser() { return new User(); }


    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/singup")
    public String signup(UserCreateForm userCreateForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "signup_form";
        //TO DO : 다른 예외처리 해주기 (이메일입력안할시 말고 다른 경우)
        if(userCreateForm.getEmail() == "") {
            bindingResult.rejectValue("email", "emaill will be not null", "이메일을 입력해주세요.");
            return "signup_form";
        }
        try {
            userService.signup(userCreateForm);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return "signup_form";
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "signup_form";
        }
        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("userInfo") String email, Model model) {
        if(this.userService.loginByEmail(email) == null) {
            return "signup_form";
        }
        UserDto userDto = this.userService.loginByEmail(email);
        //위에 어노테이션이랑 동일한 어트리뷰트 네임이면 session에 넘어가는 것 같아요.
        model.addAttribute("userInfo", userDto);

        return "redirect:/";
    }

    @GetMapping("/info")
    public String selectInfo(Model model) {
        model.getAttribute("userInfo");

        return "mypage";
    }
}
