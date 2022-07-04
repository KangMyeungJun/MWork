package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
