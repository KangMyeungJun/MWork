package com.mwork.main.querydsl;


import static com.mwork.main.entity.QMember.*;

import com.mwork.main.entity.post.Board;
import com.mwork.main.entity.post.Comment;
import com.mwork.main.entity.member.Member;
import static com.mwork.main.entity.QBoard.*;
import static org.assertj.core.api.Assertions.*;

import static com.mwork.main.entity.QComment.*;

import com.mwork.main.home.repository.CommentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
@Slf4j
public class TestComments {
    @PersistenceContext
    EntityManager em;

    @Autowired
    CommentRepository commentRepository;

    JPAQueryFactory query;
    Member member1;
    Board board1;

    @BeforeEach
    void beforeEach() {
        query = new JPAQueryFactory(em);
        member1 = query.selectFrom(member)
                .where(member.id.eq(6L))
                .fetchOne();

        board1 = query.selectFrom(board)
                .where(board.id.eq(25L))
                .fetchOne();

    }

    @Test
    void testInsertComment() {
        Comment comment1 = new Comment();
        comment1.setId(0L);
        comment1.setBoard(board1);
        comment1.setMember(member1);
        comment1.setContent("nice Comment!");
        Comment save = commentRepository.save(comment1);
        Optional<Comment> result = commentRepository.findById(save.getId());

        Comment findComment = result.get();
        assertThat(findComment.getContent()).isEqualTo(save.getContent());
    }

    @Test
    void testParentComment() {
        Comment parentComment = new Comment();
        parentComment.setBoard(board1);
        parentComment.setContent("Parent Comment!!!");
        parentComment.setMember(member1);

        commentRepository.save(parentComment);

        Comment childComment = new Comment();
        childComment.setParentComment(parentComment);
        childComment.setBoard(parentComment.getBoard());
        childComment.setMember(member1);
        childComment.setContent("Child Comment!");
        Comment saveChild = commentRepository.save(childComment);

        Comment childComment2 = new Comment();
        childComment2.setParentComment(parentComment);
        childComment2.setBoard(parentComment.getBoard());
        childComment2.setMember(member1);
        childComment2.setContent("Child Comment2!");

        Comment saveChild2 = commentRepository.save(childComment2);

        List<Comment> result = query.selectFrom(comment)
                .where(comment.parentComment().id.eq(saveChild.getParentComment().getId()))
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }
}
