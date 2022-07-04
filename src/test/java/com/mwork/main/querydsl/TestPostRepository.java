package com.mwork.main.querydsl;

import com.mwork.main.entity.post.Board;
import com.mwork.main.home.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
public class TestPostRepository {

    @Autowired
    PostRepository postRepository;

    @Test
    void testSearch() {
        PageRequest request = PageRequest.of(0,5);
        Page<Board> boards = postRepository.searchTitle("",request);
        List<Board> all = postRepository.findAll();
        for (Board board : all) {
            System.out.println("board = " + board.getTitle());
            System.out.println("board = " + board.getId());
        }
        System.out.println(boards.getTotalElements());
        for (Board board : boards) {
            System.out.println("board = " + board.getTitle());
        }
    }

//    @Test
//    void testAddCount() {
//        Board beforeBoard = postRepository.findById(26L).get();
//        log.info("before Count = {}",beforeBoard.getCount());
//        long result = postRepository.addCount(26L);
//        Board afterBoard = postRepository.findById(26L).get();
//        log.info("after Count = {}",afterBoard.getCount());
//
//    }
}
