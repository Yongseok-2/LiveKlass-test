package com.example.liveklass.service.auth;

import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.dto.auth.LoginRequest;
import com.example.liveklass.dto.auth.SignUpRequest;
import com.example.liveklass.global.error.CustomException;
import com.example.liveklass.global.error.ErrorCode;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공 - 중복 아이디가 없는 경우")
    void signUp_success() {

        // given
        SignUpRequest request = new SignUpRequest("user1234", "1234", "홍길동", MemberRole.STUDENT);

        // 아이디 중복 체크 시 false를 반환하도록 설정
        given(memberRepository.existsByUserName(request.userName())).willReturn(false);

        // 예외가 발생하지 않고 잘 실행되는지 확인
        assertDoesNotThrow(() -> authService.signUp(request));

        // 실제로 save 메서드가 호출되었는지 확인
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 아이디가 있는 경우")
    void signUp_fail() {

        // given
        SignUpRequest request = new SignUpRequest("user1234", "1234", "홍길동", MemberRole.STUDENT);

        // 아이디 중복 체크 시 ture를 반환하도록 설정
        given(memberRepository.existsByUserName(request.userName())).willReturn(true);

        // 커스텀 에러를 발생시키는지 확인
        CustomException exception = assertThrows(CustomException.class, () -> authService.signUp(request));

        // 던져진 에러 코드가 DUPLICATE_ID인지 확인
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_ID);
    }

    @Test
    @DisplayName("로그인 성공 - 아이디가 존재하고 비밀번호가 동일")
    void login_success() {

        // given
        LoginRequest request = new LoginRequest("user1234", "1234");

        Member member = Member.builder()
                .userName("user1234")
                .password("1234")
                .build();

        // 사용자 존재 체크 시 ture를 반환하도록 설정
        given(memberRepository.findByUserName(request.userName())).willReturn(Optional.of(member));

        // 예외가 발생하지 않고 잘 실행되는지 확인
        assertDoesNotThrow(() -> authService.login(request));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void login_fail_passwordMismatch() {

        LoginRequest request = new LoginRequest("user1234", "3456");

        Member member = Member.builder()
                .userName("user1234")
                .password("1234")
                .build();

        given(memberRepository.findByUserName(request.userName())).willReturn(Optional.of(member));

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LOGIN_FAILED);
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 아이디")
    void login_fail_userNotFound() {

        LoginRequest request = new LoginRequest("unknown", "1234");
        given(memberRepository.findByUserName(anyString())).willReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.LOGIN_FAILED);
    }
}
