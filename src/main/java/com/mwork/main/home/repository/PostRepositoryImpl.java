package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Board;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.mwork.main.entity.post.QBoard.board;

public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.query=new JPAQueryFactory(em);

    }

    private OrderSpecifier<?> boardSort(String sort) {
        if (sort == null) {
            return board.createdDate.desc();
        }
        if (sort.equals("count")) {
            return board.count.desc();
        }
        if (sort.equals("rating")) {
            return board.commentList.size().desc();
        }
        return board.createdDate.desc();
    }

    @Override
    public Page<Board> searchTitle(String title, Pageable pageable, String sort) {


        List<Board> results = query.selectFrom(board)
                .where(board.title.contains(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(boardSort(sort))
                .fetch();


        int size = results.size();

        return new PageImpl<>(results,pageable,size);
    }

    @Override
    public int selectBoardCnt(String title) {
        return query.select(board.count)
                .from(board)
                .where(board.title.contains(title))
                .fetch().size();
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
