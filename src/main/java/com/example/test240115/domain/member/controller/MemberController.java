package com.example.test240115.domain.member.controller;

import com.example.test240115.domain.member.dto.MemberDto;
import com.example.test240115.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/create")
    public String createForm(MemberDto memberDto){
        return "/member/create";
    }

    @PostMapping("/create")
    public String createMember(@Valid MemberDto memberDto, BindingResult br){

        br = memberService.create(memberDto, br);

        if(br.hasErrors()){
            return "/member/create";
        }

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String loginForm(){
        return "/member/login";
    }

//    @PostMapping("login")
//    public String doLogin(MemberDto memberDto, BindingResult br){
//
//        br = memberService.login(memberDto, br);
//
//        return "/member/login";
//    }

}
