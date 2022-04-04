package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.Kakao.KakaoAPI;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.form.UserLoginForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Getter
@Setter
@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
@ResponseBody
public class UserController {

    private String sns_id;
    private String email;
    private String nickname;

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
            userCreateForm.setSns_id(sns_id);
            userCreateForm.setEmail(email);
            userCreateForm.setNickname(nickname);
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

    @RequestMapping("/login")
    public UserDto login(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        UserDto userDto = null;
        KakaoAPI kakaoAPI = new KakaoAPI();
        System.out.println("code = " + code);
        String access_Token = kakaoAPI.getAccessTocken(code);

        System.out.println("Access_Token : " + access_Token);

        HashMap<String, Object> kakaoInfo = kakaoAPI.getUserInfo(access_Token);

        HttpSession session = request.getSession();
        String email = kakaoInfo.get("email").toString();
        String sns_id = kakaoInfo.get("sns_id").toString();
        String nickname = kakaoInfo.get("nickName").toString();

        try {
            userDto = this.userService.loginByEmail(email);
        }catch(DataNotFoundException e1) {
            System.out.println("이게 나오면 첫 카카오 로그인 성공");
            try {
                setSns_id(sns_id);
                setNickname(nickname);
                setEmail(email);
                //회원가입 폼으로 이동 해주기  현재 그냥 임의값 넣었음
                //signup(userCreateForm);
            }catch(DataIntegrityViolationException e2) {
                e2.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
                return userDto;
            }catch(Exception e3) {
                e3.printStackTrace();
                bindingResult.reject("signupFailed", e3.getMessage());
                return userDto;
            }
        }

        session.setAttribute("userInfo", userDto);
        model.addAttribute("userInfo", userDto);
        return userDto;
    }

    @RequestMapping("/ss")
    public UserDto emailTest(HttpServletRequest request,Model model) {
        UserDto userDto = this.userService.loginByEmail("zxz4641@daum.net");
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userDto);
        return userDto;
    }

    @RequestMapping("/info")
    public UserDto selectInfo(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");
        return userDto;
    }
}
