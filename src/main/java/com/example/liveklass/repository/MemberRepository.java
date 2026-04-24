package com.example.liveklass.repository;

import com.example.liveklass.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUserName(String userName);
    Optional<Member> findByUserName(String userName);
}
