package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.Kakao.KakaoAPI;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.form.UserLoginForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
@SessionAttributes("userInfo")
@ResponseBody
public class UserController {

    @ModelAttribute("userInfo")
    public User setUpUser() { return new User(); }


    private final UserService userService;

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public UserDto signup(UserCreateForm userCreateForm, BindingResult bindingResult) {
        UserDto dto = null;
        if(bindingResult.hasErrors())
            return dto;

        //TO DO : 다른 예외처리 해주기 (이메일입력안할시 말고 다른 경우)
        if(userCreateForm.getEmail() == "") {
            bindingResult.rejectValue("email", "emaill will be not null", "이메일을 입력해주세요.");
            return dto;
        }
        try {
            dto = userService.signup(userCreateForm);
        }catch(DataIntegrityViolationException e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            return dto;
        }catch(Exception e) {
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return dto;
        }
        return dto;
    }

    @PostMapping("/login")
    public UserDto login(HttpServletRequest req, UserLoginForm userLoginForm, Model model) {
        KakaoAPI kakaoAPI = new KakaoAPI();
        String code = req.getParameter("code");
        String access_Token = kakaoAPI.getAccessTocken(code);

        System.out.println("Access_Token : " + access_Token);

        HashMap<String, Object> kakaoInfo = kakaoAPI.getUserInfo(access_Token);

        String email = kakaoInfo.get("email").toString();
        String sns_id = kakaoInfo.get("sns_id").toString();
        //카카오 정보 받아온 후 회원가입 탭으로 이동시켜주기
        if(email == null || sns_id == null) {
            
        }



        System.out.println("@@@ : " + email);
        UserDto userDto = this.userService.loginByEmail(email);
        if(this.userService.loginByEmail(email) == null) {
            return userDto;
        }

        //위에 어노테이션이랑 동일한 어트리뷰트 네임이면 session에 넘어가는 것 같아요.
        model.addAttribute("userInfo", userDto);

        return userDto;
    }

    @GetMapping("/info")
    public UserDto selectInfo(Model model) {
        model.getAttribute("userInfo");
        UserDto userDto = (UserDto) model.getAttribute("userInfo");
        return userDto;
    }
}
