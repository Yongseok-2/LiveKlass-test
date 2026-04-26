package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.Lecture;
import com.example.liveklass.domain.Member;
import com.example.liveklass.domain.MemberRole;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.EnrollmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EnrollmentConcurrencyTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        // 1. 기존 데이터 정리 (안전한 테스트를 위해)
        enrollmentRepository.deleteAll();

        // 2. 테스트용 유저 100명 생성 및 저장
        List<Member> testStudents = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testStudents.add(Member.builder()
                    .userName("testUser" + i)
                    .password("1234")
                    .name("테스트유저" + i)
                    .role(MemberRole.STUDENT)
                    .build());
        }
        memberRepository.saveAllAndFlush(testStudents); // 한 번에 100명 저장!
    }

    @AfterEach
    void tearDown() {
        enrollmentRepository.deleteAll(); // 신청 내역 깔끔하게 비우기
        // 테스트용으로 생성한 유저들만 골라서 삭제
        memberRepository.deleteByUserNameStartsWith("testUser");

    }

    @Test
    @DisplayName("100명이 동시에 수강 신청을 했을 때, 정원이 초과되지 않아야 한다")
    void concurrencyTest() throws InterruptedException {

        // Given
        int threadCount = 100; // 100명 동시 접속
        Long lectureId = 2L;   // 테스트용 강의 ID (정원이 30명이라고 가정)
        String baseUserName = "testUser";

        // 멀티스레드 환경을 위한 도구
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 32개의 스레드 풀
        CountDownLatch latch = new CountDownLatch(threadCount); // 100개의 작업이 끝날 때까지 대기

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("수강 신청 동시성 테스트");

        // When
        for (int i = 0; i < threadCount; i++) {
            String userName = baseUserName + i; // 각기 다른 유저 아이디 생성
            executorService.submit(() -> {
                try {
                    enrollmentService.subEnrollment(lectureId, userName);
                    System.out.println("신청완료: " + userName);
                } catch (Exception e) {
                    // 정원 초과 등으로 인한 예외는 로그만 남기고 테스트 진행
                    System.out.println("신청 실패: " + e.getMessage());
                } finally {
                    latch.countDown(); // 작업 완료 신호
                }
            });
        }

        latch.await(); // 모든 스레드의 작업이 끝날 때까지 메인 스레드 대기

        stopWatch.stop();

        // Then
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        System.out.println("=======================================");
        System.out.println("최종 수강 인원: " + lecture.getCurrentEnrollmentCount());
        System.out.println("총 소요 시간: " + stopWatch.getTotalTimeMillis() + "ms"); // 밀리초 단위
        System.out.println(stopWatch.prettyPrint()); // 상세 보고서 출력
        System.out.println("=======================================");

        // 정원이 30명이라면, 최종 인원이 30을 넘으면 동시성 이슈 발생!
        assertThat(lecture.getCurrentEnrollmentCount()).isLessThanOrEqualTo(lecture.getMaxCapacity());
    }
}
