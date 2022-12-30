package com.example.sp2.controller;

import com.example.sp2.dto.SignupRequestDto;
import com.example.sp2.security.UserDetailsImpl;
import com.example.sp2.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원 로그인 페이지
    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("username");
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/user/signup")
    public String signup() {
        return "signup";
    }

    // 회원 가입 요청 처리
    @ResponseBody
    @PostMapping("/user/signup")
    public String registerUser(@RequestBody SignupRequestDto requestDto) {
        return userService.registerUser(requestDto);
    }

    @PostMapping("/user/nameget")
    public String jwt(UserDetailsImpl userDetails) {
        return userService.jwt(userDetails);
    }
    @ResponseBody
    @GetMapping("/user/test")
    public String test(@RequestHeader(value = "token", defaultValue = "token") String token) {
        return userService.decodeUsername(token);
    }
}