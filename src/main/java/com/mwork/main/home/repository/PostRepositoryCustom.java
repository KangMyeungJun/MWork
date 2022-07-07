package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {


    Page<Board> searchTitle(String title, Pageable pageable, String sort);

    int selectBoardCnt(String title);

    long addCount(Long id);
    long deleteBoard(Long id);

}
