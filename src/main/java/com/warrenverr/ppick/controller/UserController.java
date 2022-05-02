package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.GoogleAPI.GoogleAPI;
import com.warrenverr.ppick.Kakao.KakaoAPI;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.form.UserCreateForm;
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
import javax.validation.Valid;
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
            bindingResult.rejectValue("email", "email will be not null", "이메일을 입력해주세요.");
            return dto;
        }
        try {
            userCreateForm.setSns_id(sns_id);
            userCreateForm.setEmail(email);
            //구글 로그인은 nickname을 안받아오므로 구글에서는 ""칸 주입
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


    @RequestMapping("/Google_login")
    public UserDto GoogleLogin(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        UserDto userDto = null;
        GoogleAPI googleAPI = new GoogleAPI();
        System.out.println("Google code = " + code);
        String access_Token = googleAPI.getAccessToken(code);

        System.out.println("Access_Token : " + access_Token);
        HashMap<String, Object> googleInfo = googleAPI.getUserInfo(access_Token);

        HttpSession session = request.getSession();
        String email = googleInfo.get("email").toString();
        String sns_id = googleInfo.get("sns_id").toString();
        String nickname = "";
        try {
            userDto = this.userService.loginByEmail(email);
        }catch(DataNotFoundException e1) {
            System.out.println("이게 나오면 첫 구글 로그인 성공");
            try {
                setSns_id(sns_id);
                setEmail(email);
                setNickname(nickname);
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


    @RequestMapping("/Kakao_login")
    public UserDto login(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        UserDto userDto = null;
        KakaoAPI kakaoAPI = new KakaoAPI();
        System.out.println("Kakao code = " + code);
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

    @RequestMapping("/HardCoding_kakaoLogin_getSession")
    public UserDto emailTest(HttpServletRequest request,Model model) {
        UserDto userDto = this.userService.loginByEmail("dbswl@naver.com");
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userDto);
        return userDto;
    }


    @RequestMapping("/JustLogin")
    public UserDto JustLogin(@ModelAttribute("email") String email, HttpServletRequest request,Model model) {
        UserDto userDto = this.userService.loginByEmail(email);
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

    @PostMapping("/modify")
    public String userModify(@Valid UserCreateForm userCreateForm, BindingResult bindingResult, HttpServletRequest request) {

        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        if(bindingResult.hasErrors())
            return "usermodi_form";
        userCreateForm.setSns_id(userDto.getSns_id());
        userCreateForm.setEmail(userDto.getEmail());
        userCreateForm.setNickname(userDto.getNickname());
        this.userService.modify(userDto, userCreateForm);
        return "mainPage";
    }

    @GetMapping("/delete")
    public String userDelete(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        this.userService.delete(userDto);
        return "mainPage";
    }
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();

        return "mainPage";
    }


}
