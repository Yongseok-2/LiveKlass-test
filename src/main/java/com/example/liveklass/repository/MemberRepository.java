package com.example.liveklass.repository;

import com.example.liveklass.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserName(String userName);
    Optional<Member> findByUserName(String userName);

    @Transactional
    void deleteByUserNameStartsWith(String testUser);
}
