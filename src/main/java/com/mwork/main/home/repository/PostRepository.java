package com.mwork.main.home.repository;

import com.mwork.main.entity.post.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Board, Long>,PostRepositoryCustom {

    @Override
    @Query("select b from Board b join fetch b.member")
    List<Board> findAll();





}
