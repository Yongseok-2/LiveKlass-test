package com.example.liveklass.service;

import com.example.liveklass.domain.Member;
import com.example.liveklass.dto.auth.LoginRequest;
import com.example.liveklass.dto.auth.SignUpRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signUp(SignUpRequest request) {

        if(memberRepository.existsByUserName(request.userName())) {
            throw new CustomException(ErrorCode.DUPLICATE_ID);
        }

        Member member = Member.builder()
                .userName(request.userName())
                .password(request.password())
                .name(request.name())
                .role(request.role())
                .build();

        memberRepository.save(member);
    }

    public Member login(LoginRequest request) {
        Member member = memberRepository.findByUserName(request.userName())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_FAILED));

        if(!member.getPassword().equals(request.password())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        return member;
    }
}
