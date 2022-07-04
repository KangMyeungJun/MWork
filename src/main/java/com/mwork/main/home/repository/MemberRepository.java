package com.mwork.main.home.repository;

import com.mwork.main.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByoAuth2Id(@Param("oAuth2Id") String oAuth2Id);
}
