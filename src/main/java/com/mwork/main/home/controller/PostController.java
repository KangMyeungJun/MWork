package com.mwork.main.home.controller;

import com.mwork.main.auth.Oauth2Service;
import com.mwork.main.entity.post.Board;
import com.mwork.main.entity.post.BoardForm;
import com.mwork.main.entity.post.Comment;
import com.mwork.main.entity.post.CommentForm;
import com.mwork.main.entity.member.Member;
import com.mwork.main.home.repository.MemberRepository;
import com.mwork.main.home.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final MemberRepository memberRepository;
    private final PostService postService;
    private final Oauth2Service oauth2Service;

    @GetMapping("/")
    public String addPost() {
        return "post";
    }


    @PostMapping (value = "/")
    public String postForm(BoardForm boardForm,@SessionAttribute String oauth2id) {
        Board board = new Board();
        board.setArticle(boardForm.getResult());
        board.setTitle(boardForm.getTitle());
        Member member = oauth2Service.findBySocialId(oauth2id);
        board.setMember(member);

        Board savedBoard = postService.saveBoard(board);


        log.info("savedBoard = {}",savedBoard);
        return "redirect:/post/";
    }

    @GetMapping("/pages")
    public String postPages(@PageableDefault(size = 9, sort="createdDate",direction = Sort.Direction.DESC) Pageable pageable,
                            Model model,
                            @RequestParam(value = "keyword",defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "5") Integer size) {
//postService.findAll(pageable);
        log.info("keyword = {}",keyword);
        Page<Board> all = postService.findAllByTitle(keyword,pageable);

        model.addAttribute("posts",all);
        model.addAttribute("size",size);

        return "pages";
    }

    @GetMapping("/{id}")
    public String pageDetail(@PathVariable Long id,Model model,
                             @SessionAttribute(required = false) Long accountId) {

        postService.addCount(id);
        Optional<Board> findPost = postService.findByIdBoard(id);
        Board findGetPost = findPost.get();

        if (accountId != null && findGetPost.getMember().getId() == accountId) {
            model.addAttribute("udCondition",true);
        }
        model.addAttribute("item",findGetPost);
        return "detail";
    }

    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Optional<Board> findPost = postService.findByIdBoard(id);
        Board findGetPost = findPost.get();
        model.addAttribute("item", findGetPost);

        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmitPost(@PathVariable Long id,BoardForm boardForm) {
        Board board = postService.findByIdBoard(id).get();
        board.setTitle(boardForm.getTitle());
        board.setArticle(boardForm.getResult());
        board.setModifiedDate(LocalDateTime.now());

        postService.saveBoard(board);
        return "redirect:/post/{id}";
    }

    @PostMapping("/delete/{id}")
    public String delBoard(@PathVariable Long id) {
        long delCount = postService.deleteBoard(id);
        log.info("delCount = {}",delCount);

        return "redirect:/post/";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, CommentForm commentForm,
                             @SessionAttribute Long accountId) {
        Member member = memberRepository.findById(accountId).get();
        Board findBoard = postService.findByIdBoard(id).get();

        Comment comment = new Comment();
        comment.setBoard(findBoard);
        comment.setContent(commentForm.getContent());
        comment.setMember(member);
        postService.saveComment(comment);

        return "redirect:/post/{id}";
    }



}
