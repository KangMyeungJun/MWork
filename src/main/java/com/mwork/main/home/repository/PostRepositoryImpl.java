package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Board;
import static com.mwork.main.entity.post.QBoard.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.query=new JPAQueryFactory(em);

    }

    @Override
    public Page<Board> searchTitle(String title, Pageable pageable) {
        List<Board> results = query.selectFrom(board)
                .where(board.title.contains(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int size = results.size();

        return new PageImpl<>(results,pageable,size);
    }

    @Override
    @Transactional
    @Modifying
    public long addCount(Long id) {
        return query.update(board)
                .set(board.count, board.count.add(1))
                .where(board.id.eq(id))
                .execute();
    }

    @Override
    @Transactional
    @Modifying
    public long deleteBoard(Long id) {
        return query.delete(board)
                .where(board.id.eq(id))
                .execute();
    }


}
