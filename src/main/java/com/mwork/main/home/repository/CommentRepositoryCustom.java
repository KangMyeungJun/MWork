package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findCommentByIdExcludeChild(Long id);

}
