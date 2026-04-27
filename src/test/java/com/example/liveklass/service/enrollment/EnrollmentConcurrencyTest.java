package com.example.liveklass.service.enrollment;

import com.example.liveklass.domain.*;
import com.example.liveklass.dto.lecture.LectureUpdateRequest;
import com.example.liveklass.repository.EnrollmentRepository;
import com.example.liveklass.repository.LectureRepository;
import com.example.liveklass.repository.MemberRepository;
import com.example.liveklass.service.CreatorService;
import com.example.liveklass.service.EnrollmentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EnrollmentConcurrencyTest {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CreatorService creatorService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {

        // 테스트용 유저 100명 생성 및 저장
        List<Member> testStudents = new ArrayList<>();
        for (int i = 100; i < 200; i++) {
            testStudents.add(Member.builder()
                    .userName("testUser" + i)
                    .password("1234")
                    .name("테스트유저" + i)
                    .role(MemberRole.STUDENT)
                    .build());
        }
        memberRepository.saveAllAndFlush(testStudents); // 한 번에 100명 저장
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
        Long lectureId = 9L;   // 테스트용 강의 ID (정원이 30명이라고 가정)
        String baseUserName = "testUser";

        // 멀티스레드 환경을 위한 도구
        ExecutorService executorService = Executors.newFixedThreadPool(32); // 32개의 스레드 풀
        CountDownLatch latch = new CountDownLatch(threadCount); // 100개의 작업이 끝날 때까지 대기

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("수강 신청 동시성 테스트");

        // When
        for (int i = 100; i < 200; i++) {
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
        System.out.println("대기열 인원: " + lecture.getWaitCount());
        System.out.println("총 소요 시간: " + stopWatch.getTotalTimeMillis() + "ms"); // 밀리초 단위
        System.out.println(stopWatch.prettyPrint()); // 상세 보고서 출력
        System.out.println("=======================================");

        // 정원이 30명이라면, 최종 인원이 30을 넘으면 동시성 이슈 발생!
        assertThat(lecture.getCurrentEnrollmentCount()).isLessThanOrEqualTo(lecture.getMaxCapacity());
    }

    @Test
    @DisplayName("5명이 동시에 수강을 취소했을 때, 대기자 5명이 누락 없이 모두 승격되어야 한다")
    void waitlistPromotionConcurrencyTest() throws InterruptedException {

        // given
        // 10번 강의: 정원 30/30, 대기자 5명(ID 1001~1005)으로 세팅됨
        Long lectureId = 10L;

        // 취소할 대상자 5명의 정보를 DB에서 가져옴 (student01 ~ student05)
        List<String> cancelUserNames = List.of("student01", "student02", "student03", "student04", "student05");
        List<Long> enrollmentIdsToCancel = new ArrayList<>();

        for (String name : cancelUserNames) {
            enrollmentRepository.findByMemberUserNameAndLectureId(name, lectureId)
                    .ifPresent(e -> enrollmentIdsToCancel.add(e.getId()));
        }

        int threadCount = enrollmentIdsToCancel.size(); // 5명
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("대기열 자동 승격 동시성 테스트");

        // [When] 5명이 동시에 취소 요청
        for (int i = 0; i < threadCount; i++) {
            Long targetEnrollmentId = enrollmentIdsToCancel.get(i);
            String targetUserName = cancelUserNames.get(i);

            executorService.submit(() -> {
                try {
                    // 핵심 로직 호출: 취소 -> 인원 감소 -> 대기자 승격(재귀)
                    enrollmentService.cancelEnrollment(targetEnrollmentId, targetUserName);
                    System.out.println("취소 성공: " + targetUserName);
                } catch (Exception e) {
                    System.out.println("취소 실패 [" + targetUserName + "]: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        stopWatch.stop();

        // [Then] 결과 검증
        System.out.println("=======================================");
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        System.out.println("최종 수강 인원: " + lecture.getCurrentEnrollmentCount());
        System.out.println("남은 대기 인원: " + lecture.getWaitCount());
        System.out.println("총 소요 시간: " + stopWatch.getTotalTimeMillis() + "ms");
        System.out.println("=======================================");

        // 1. 수강 인원은 여전히 30명이어야 함 (5명 취소된 자리를 대기자 5명이 바로 채웠으므로)
        assertThat(lecture.getCurrentEnrollmentCount()).isEqualTo(30);

        // 2. 대기자 수는 0명이 되어야 함
        assertThat(lecture.getWaitCount()).isEqualTo(0);

        // 3. 실제 대기자였던 유저들(ID: 1001~1005)의 상태가 PENDING으로 바뀌었는지 확인
        for (long id = 1001; id <= 1005; id++) {
            Enrollment promoted = enrollmentRepository.findById(id).orElseThrow();
            assertThat(promoted.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
        }
    }

    @Test
    @DisplayName("최대 정원을 늘렸을 때, 대기자 5명이 누락 없이 모두 승격되어야 한다")
    void updateMaxCapacityConcurrencyTest() throws InterruptedException {

        // given
        Long lectureId = 10L;

        LectureUpdateRequest request = new LectureUpdateRequest(
                "수정된 제목", "설명", 40, 50000L, LectureType.VOD,
                null, null, null, null
        );


        StopWatch stopWatch = new StopWatch();
        stopWatch.start("대기열 자동 승격 동시성 테스트");

        // [When]
        creatorService.updateLecture(request, lectureId, "creator02");

        stopWatch.stop();

        // [Then] 결과 검증
        System.out.println("=======================================");
        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow();
        System.out.println("최종 수강 인원: " + lecture.getCurrentEnrollmentCount());
        System.out.println("최대 수강 인원: " + lecture.getMaxCapacity());
        System.out.println("남은 대기 인원: " + lecture.getWaitCount());
        System.out.println("총 소요 시간: " + stopWatch.getTotalTimeMillis() + "ms");
        System.out.println("=======================================");

        // 1. 수강 인원은 35이어야함(대기자 5명 승격)
        assertThat(lecture.getCurrentEnrollmentCount()).isEqualTo(35);

        // 2. 대기자 수는 0명이 되어야 함
        assertThat(lecture.getWaitCount()).isEqualTo(0);

        // 3. 실제 대기자였던 유저들(ID: 1001~1005)의 상태가 PENDING으로 바뀌었는지 확인
        for (long id = 1001; id <= 1005; id++) {
            Enrollment promoted = enrollmentRepository.findById(id).orElseThrow();
            assertThat(promoted.getStatus()).isEqualTo(EnrollmentStatus.PENDING);
        }

        // 4. 수강 최대 인원이 40이어야 함
        assertThat(lecture.getMaxCapacity()).isEqualTo(40);
    }
}
