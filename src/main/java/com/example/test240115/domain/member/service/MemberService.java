package com.example.test240115.domain.member.service;

import com.example.test240115.domain.member.dto.MemberDto;
import com.example.test240115.domain.member.entity.Member;
import com.example.test240115.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberSecurityService memberSecurityService;
    private final PasswordEncoder passwordEncoder;

    public Member findByLoginId(String loginid){

        Optional<Member> member = memberRepository.findByLoginId(loginid);

        return member.get();
    }

    @Transactional
    public BindingResult create(MemberDto memberDto, BindingResult br) {

        if(br.hasErrors()){
            return br;
        }

        // 검증 메소드로 묶는게 나을듯
        if(!memberDto.getPassword().equals(memberDto.getPasswordConfirm())){
            br.reject("passwordConfirm","비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return br;
        }
        Optional<Member> memberFindLoginId = memberRepository.findByLoginId(memberDto.getLoginId());

        if(memberFindLoginId.isPresent()){
            br.reject("duplication LoginId","해당 LoginId로 이미 가입되어 있습니다.");
            return br;
        }

        Optional<Member> memberFindNickname = memberRepository.findByNickname(memberDto.getNickname());

        if(memberFindNickname.isPresent()){
            br.reject("duplication Nickname","이미 존재하는 닉네임 입니다.");
            return br;
        }

        Member member = Member.builder()
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .createDate(LocalDateTime.now())
                .modifyDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);


        return br;
    }

    @Transactional
    public BindingResult login(MemberDto memberDto, BindingResult br) {

        if(memberDto.getLoginId().isEmpty() || memberDto.getPassword().isEmpty()){
            br.rejectValue("입력값 오류","아이디 또는 비밀번호를 확인해주세요");
            return br;
        }

        Optional<Member> member = memberRepository.findByLoginId(memberDto.getLoginId());

        if(member.isEmpty()){
            br.rejectValue("회원 없음","해당 아이디로 등록된 회원이 없습니다");
            return br;
        }

        if(!passwordEncoder.matches(memberDto.getPassword(),member.get().getPassword())){
            br.rejectValue("비밀번호 일치 오류","비밀번호가 일치하지 않습니다.");
            return br;
        }

        memberSecurityService.loadUserByUsername(member.get().getLoginId());


        return br;
    }
}
