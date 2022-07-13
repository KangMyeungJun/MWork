package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mwork.main.entity.post.QComment.comment;


public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory query;

    public CommentRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    @Override
    public List<Comment> findCommentByIdExcludeChild(Long id) {
        return query.selectFrom(comment)
                .where(comment.board().id.eq(id)
                        .and(comment.parentComment().isNull()))
                .fetch();
    }

}
