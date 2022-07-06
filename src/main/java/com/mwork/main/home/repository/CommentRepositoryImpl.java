package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Comment;
import static com.mwork.main.entity.post.QComment.*;

import com.mwork.main.entity.post.DelFlag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


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
