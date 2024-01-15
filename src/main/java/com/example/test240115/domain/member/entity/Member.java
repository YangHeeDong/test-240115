package com.example.test240115.domain.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message="아이디 필수")
    @Column(unique = true)
    private String loginId;

    @NotEmpty(message="닉네임 필수")
    @Column(unique = true)
    private String nickname;

    @NotEmpty(message="비밀번호 필수")
    private String password;

    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
}
