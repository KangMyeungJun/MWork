package com.mwork.main.home.repository;

import com.mwork.main.entity.member.Member;
import com.mwork.main.entity.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long>,CommentRepositoryCustom {

    @Transactional(readOnly = true)
    List<Comment> findByBoardId(Long id);

    @Transactional(readOnly = true)
    List<Comment> findCommentsByMember(Member member);


}
