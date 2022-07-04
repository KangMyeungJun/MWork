package com.mwork.main.home.service;

import com.mwork.main.entity.post.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Test
    void testFindAll() {
        PageRequest request = PageRequest.of(10,10);
        Page<Board> all =  postService.findAll(request);


    }

}