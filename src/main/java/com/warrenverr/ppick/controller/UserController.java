package com.warrenverr.ppick.controller;

import com.warrenverr.ppick.DataNotFoundException;
import com.warrenverr.ppick.GitHubAPI.GitHubAPI;
import com.warrenverr.ppick.GoogleAPI.GoogleAPI;
import com.warrenverr.ppick.Kakao.KakaoAPI;
import com.warrenverr.ppick.dto.ProjectDto;
import com.warrenverr.ppick.dto.UserDto;
import com.warrenverr.ppick.email.GoogleEmailService;
import com.warrenverr.ppick.form.UserCreateForm;
import com.warrenverr.ppick.model.User;
import com.warrenverr.ppick.service.ProjectService;
import com.warrenverr.ppick.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@RequestMapping("/api/user")
@RestController
@ResponseBody
public class UserController {

    private String snsid;
    private String email;
    private String nickname;

    /*@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;*/

    private final UserService userService;

    private final ProjectService projectService;

    private final GoogleEmailService googleEmailService;

    public UserDto getUserSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserDto userDto = (UserDto) session.getAttribute("userInfo");

        return userDto;
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup_form";
    }

    @PostMapping("/signup")
    public UserDto signup(@RequestBody UserCreateForm userCreateForm, BindingResult bindingResult) {
        UserDto dto = null;
        if(bindingResult.hasErrors())
            return dto;

        //TO DO : 다른 예외처리 해주기 (이메일입력안할시 말고 다른 경우)
        if(userCreateForm.getEmail() == "") {
            bindingResult.rejectValue("email", "email will be not null", "이메일을 입력해주세요.");
            return dto;
        }
        try {
//            userCreateForm.setSnsid(snsid);
//            userCreateForm.setEmail(email);
//            //구글 로그인은 nickname을 안받아오므로 구글에서는 ""칸 주입
//            userCreateForm.setNickname(nickname);
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

    @RequestMapping("/auth/GitHub_login")
    public ResponseEntity<?> GitHubLogin(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        String token = null;
        UserDto userDto = null;
        GitHubAPI githubAPI = new GitHubAPI();
        String access_Token = githubAPI.getAccessTocken(code);
        HashMap<String, Object> githubInfo = githubAPI.getUserInfo(access_Token);
        String email = githubInfo.get("email").toString();
        String snsid = githubInfo.get("sns_id").toString();
        String nickname = githubInfo.get("nickname").toString();

        try {
            userDto = this.userService.loginBySnsid(snsid);
            /*token = jwtTokenUtil.generateToken(snsid);*/
        } catch(DataNotFoundException e1) {
            System.out.println("이게 나오면 첫 깃허브 로그인 성공");
            try {
                setSnsid(snsid);
                setEmail(email);
                setNickname(nickname);
            }catch(DataIntegrityViolationException e2) {
                e2.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            }catch(Exception e3) {
                e3.printStackTrace();
                bindingResult.reject("signupFailed", e3.getMessage());
            }
        }catch(Exception e) {
            e.printStackTrace();;
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }



    @RequestMapping("/auth/Google_login")
    public UserDto GoogleLogin(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        UserDto userDto = null;
        GoogleAPI googleAPI = new GoogleAPI();
        System.out.println("Google code = " + code);
        String access_Token = googleAPI.getAccessToken(code);

        System.out.println("Access_Token : " + access_Token);
        HashMap<String, Object> googleInfo = googleAPI.getUserInfo(access_Token);

        HttpSession session = request.getSession();
        String email = googleInfo.get("email").toString();
        String snsid = googleInfo.get("sns_id").toString();
        String nickname = "";
        try {
            userDto = this.userService.loginBySnsid(snsid);
        }catch(DataNotFoundException e1) {
            System.out.println("이게 나오면 첫 구글 로그인 성공");
            try {
                setSnsid(snsid);
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


    @RequestMapping("/auth/Kakao_login")
    public ResponseEntity<?> login(@ModelAttribute("code") String code, HttpServletRequest request, UserCreateForm userCreateForm, Model model, BindingResult bindingResult) {
        UserDto userDto = null;
        String token = null;
        KakaoAPI kakaoAPI = new KakaoAPI();
        System.out.println("Kakao code = " + code);
        String access_Token = kakaoAPI.getAccessTocken(code);

        System.out.println("Access_Token : " + access_Token);

        HashMap<String, Object> kakaoInfo = kakaoAPI.getUserInfo(access_Token);

        HttpSession session = request.getSession();
        String email = kakaoInfo.get("email").toString();
        String snsid = kakaoInfo.get("sns_id").toString();
        String nickname = kakaoInfo.get("nickName").toString();


        try {
            userDto = this.userService.loginBySnsid(snsid);
            /*token = jwtTokenUtil.generateToken(snsid);*/
        }catch(DataNotFoundException e1) {
            System.out.println("이게 나오면 첫 카카오 로그인 성공");
            try {
                setSnsid(snsid);
                setNickname(nickname);
                setEmail(email);

                //회원가입 폼으로 이동 해주기  현재 그냥 임의값 넣었음
                //signup(userCreateForm);
            }catch(DataIntegrityViolationException e2) {
                e2.printStackTrace();
                bindingResult.reject("signupFailed", "이미 등록된 사용자 입니다.");
            }catch(Exception e3) {
                e3.printStackTrace();
                bindingResult.reject("signupFailed", e3.getMessage());
            }
        }

        session.setAttribute("userInfo", userDto);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer "+token);
        return ResponseEntity.ok(token);
    }
    @RequestMapping("/image")
    public void uploadImage(HttpServletRequest request, MultipartFile multi,  UserCreateForm userCreateForm) {
        //String imageName = userCreateForm.getImage();
        //String imagePath = request.getContextPath().toString();
        //String path = "C:\\ppick_image";
        //System.out.println("sss : " + multi.getOriginalFilename());
        //File fileSabe = new File(path, imageName);

    }

/*
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
*/

/*
    @RequestMapping("/HardCoding_kakaoLogin_getSession")
    public UserDto emailTest(HttpServletRequest request,Model model) {
        UserDto userDto = this.userService.loginBySnsId("dbswl@naver.com");
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", userDto);
        return userDto;
    }*/

    /*@RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(final HttpServletRequest req, final HttpServletResponse res, @Valid @RequestBody Token.Request request) throws Exception {
        UserDto userDto = this.userService.loginBySnsid(request.getId());
        Authentication authentication = new UserAuthentication(request.getId(), null, null);
        String token = JwtTokenProvider.generateToken(authentication);



        Token.Response response = Token.Response.builder().token(token).build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/


    @RequestMapping("/JustLogin")
    public UserDto JustLogin(@ModelAttribute("sns_id") String sns_id, HttpServletRequest request,Model model) {
        UserDto userDto = this.userService.loginBySnsid(sns_id);
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
        userCreateForm.setSnsid(userDto.getSnsid());
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

    @PostMapping("/approve/{id}")
    public String projectApprove(@PathVariable("id") Integer id, @RequestParam(value = "projectId") Integer projectId, HttpServletRequest request) {
        ProjectDto projectDto = this.projectService.getProjectByPid(projectId);
        UserDto userDto = getUserSession(request);
        this.userService.approve(projectDto, id);
        return String.format("redirect:/project/detail/%s", id);
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {

        List<UserDto> userList = userService.findAllUser();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/ppick")
    public ResponseEntity<?> userPpick() {
        googleEmailService.sendMail("ktykty0722@naver.com");
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
