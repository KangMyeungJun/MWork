package com.mwork.main.home.service;

import com.mwork.main.entity.post.Comment;
import com.mwork.main.home.repository.CommentRepository;
import com.mwork.main.home.repository.PostRepository;
import com.mwork.main.entity.post.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Board saveBoard(Board board) {
        return postRepository.save(board);
    }

    public Page<Board> findAllByTitle(String title, Pageable pageable) {
        return postRepository.searchTitle(title,pageable);
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Optional<Board> findByIdBoard(Long id) {
        return postRepository.findById(id);
    }

    public long addCount(Long id) {
        return postRepository.addCount(id);
    }

    public long deleteBoard(Long id) {
        return postRepository.deleteBoard(id);
    }

    public Optional<Comment> findByIdComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }



}
