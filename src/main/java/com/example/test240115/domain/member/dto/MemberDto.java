package com.example.test240115.domain.member.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotEmpty(message="아이디 필수")
    @Column(unique = true)
    private String loginId;

    @NotEmpty(message="닉네임 필수")
    @Column(unique = true)
    private String nickname;

    @NotEmpty(message="비밀번호 필수")
    private String password;

    @NotEmpty(message="비밀번호 필수")
    private String passwordConfirm;
}
